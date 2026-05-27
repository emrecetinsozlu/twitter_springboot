package com.twitter.demo.tweet.service;

import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.exception.UserNotFoundException;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.tweet.dto.TweetCreateRequest;
import com.twitter.demo.tweet.dto.TweetMapper;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.dto.TweetUpdateRequest;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.service.UnknownServiceException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    public TweetServiceImpl(TweetRepository tweetRepository,  UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;

    }

    @Override
    @Transactional
    public TweetResponse createTweet(TweetCreateRequest tweetCreateRequest) {

        User user = userRepository.findById(tweetCreateRequest.userId()).orElseThrow(() -> new UserNotFoundException("User bulunamadı"));
        Tweet tweet = Tweet.builder()
                .content(tweetCreateRequest.content())
                .user(user)
                .build();

        Tweet createdTweet = tweetRepository.save(tweet);

        return TweetMapper.toTweetResponse(createdTweet);
    }

    @Override
    @Transactional
    public List<TweetResponse> findByUserId(Long userId) {
        return tweetRepository.findByUserId(userId).stream().map(TweetMapper::toTweetResponse).toList();

    }

    @Override
    public TweetResponse findById(Long tweetId) {
        return tweetRepository.findById(tweetId).map(TweetMapper::toTweetResponse).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

    }

    @Override
    public TweetResponse updateTweet(Long userId,Long tweetId, TweetUpdateRequest tweetUpdateRequest) {

        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        if (!tweet.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("User is not authorized to update this tweet");
        }
        tweet.setContent(tweetUpdateRequest.content());

        //@Transcational kullanırsam save ile kaydetmeme gerek yok otomatikman kendi kaydederdi
        Tweet updatedTweet = tweetRepository.save(tweet);
        return TweetMapper.toTweetResponse(updatedTweet);


    }

    @Override
    public void deleteTweet(Long tweetId, Long userId) {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new RuntimeException("Tweet not found"));
        if (!tweet.getUser().getId().equals(userId)) {
            throw new RuntimeException("User is not authorized to delete this tweet");
        }
        tweetRepository.delete(tweet);
    }
}
