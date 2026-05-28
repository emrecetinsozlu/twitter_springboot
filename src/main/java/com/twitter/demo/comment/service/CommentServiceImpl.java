package com.twitter.demo.comment.service;

import com.twitter.demo.comment.Comment;
import com.twitter.demo.comment.CommentMapper;
import com.twitter.demo.comment.CommentRepository;
import com.twitter.demo.comment.dto.CommentCreateRequest;
import com.twitter.demo.comment.dto.CommentResponse;
import com.twitter.demo.comment.dto.CommentUpdateRequest;
import com.twitter.demo.exception.ForbiddenException;
import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.security.CurrentUserService;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final CurrentUserService currentUserService;


    @Override
    @Transactional
    public CommentResponse createComment(CommentCreateRequest commentCreateRequest) {

        User currentUser = currentUserService.getCurrentUser();

        //User user = userRepository.findById(commentCreateRequest.userId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Tweet tweet = tweetRepository.findById(commentCreateRequest.tweetId()).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
        Comment comment = Comment.builder()
                .content(commentCreateRequest.content())
                .user(currentUser)
                .tweet(tweet)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toResponse(savedComment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(CommentUpdateRequest commentUpdateRequest, Long commentId) {
         User currentUser = currentUserService.getCurrentUser();
         Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
         if (!comment.getUser().getId().equals(currentUser.getId())) {
             throw new ForbiddenException("You are not allowed to update this comment");
         }
         comment.setContent(commentUpdateRequest.content());

        return CommentMapper.toResponse(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        User currentUser = currentUserService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        Long commentOwnerId = comment.getId();
        Long tweetOwnerId = comment.getTweet().getUser().getId();

        boolean isCommentOwner = commentOwnerId.equals(currentUser.getId());
        boolean isTweetOwner = tweetOwnerId.equals(currentUser.getId());
        if(!isCommentOwner && !isTweetOwner) {
            throw new ForbiddenException("You are not allowed to delete this comment");
        }
        commentRepository.delete(comment);

    }
}
