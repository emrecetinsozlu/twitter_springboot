package com.twitter.demo.like.service;


import com.twitter.demo.exception.BadRequestException;
import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.like.Like;
import com.twitter.demo.like.dto.LikeCreateRequest;
import com.twitter.demo.like.dto.LikeMapper;
import com.twitter.demo.like.dto.LikeResponse;
import com.twitter.demo.like.dto.LikedUserDTO;
import com.twitter.demo.like.repository.LikeRepository;
import com.twitter.demo.security.CurrentUserService;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public LikeResponse likeTweet(LikeCreateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + currentUser.getId()
                ));

        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet not found with id: " + request.tweetId()
                ));

        boolean alreadyLiked = likeRepository.existsByUserIdAndTweetId(
                currentUser.getId(),
                request.tweetId()
        );

        if (alreadyLiked) {
            throw new BadRequestException("User already liked this tweet");
        }

        Like like = Like.builder()
                .user(user)
                .tweet(tweet)
                .build();

        Like savedLike = likeRepository.save(like);

        return LikeMapper.toResponse(savedLike);
    }

    @Override
    @Transactional
    public void dislikeTweet(LikeCreateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Like like = likeRepository.findByUserIdAndTweetId(
                currentUser.getId(),
                request.tweetId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Like not found for userId: " + currentUser.getId()
                        + " and tweetId: " + request.tweetId()
        ));

        likeRepository.delete(like);
    }

    @Override
    public void deleteAllByUserId(Long userId) {

        likeRepository.deleteAllByUserId(userId);
    }
    /*

    @Override
    public Long countByTweetId(Long tweetId) {
        return likeRepository.countByTweetId(tweetId);
    }

    @Override
    public List<LikedUserDTO> getLikedUsers(Long tweetId) {
        return likeRepository.findAllByTweetId(tweetId)
                .stream()
                .map(LikeMapper::toLikedUserDTO)
                .toList();
    }

    */

}
