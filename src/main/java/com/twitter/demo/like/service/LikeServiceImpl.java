package com.twitter.demo.like.service;


import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.like.Like;
import com.twitter.demo.like.dto.LikeCreateRequest;
import com.twitter.demo.like.dto.LikeMapper;
import com.twitter.demo.like.dto.LikeResponse;
import com.twitter.demo.like.repository.LikeRepository;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public LikeResponse likeTweet(LikeCreateRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.userId()
                ));

        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet not found with id: " + request.tweetId()
                ));

        boolean alreadyLiked = likeRepository.existsByUserIdAndTweetId(
                request.userId(),
                request.tweetId()
        );

        if (alreadyLiked) {
            throw new RuntimeException("User already liked this tweet");
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

        Like like = likeRepository.findByUserIdAndTweetId(
                request.userId(),
                request.tweetId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Like not found for userId: " + request.userId()
                        + " and tweetId: " + request.tweetId()
        ));

        likeRepository.delete(like);
    }

}
