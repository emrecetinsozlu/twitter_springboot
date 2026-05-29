package com.twitter.demo.tweet.service;

import com.twitter.demo.tweet.dto.TweetCreateRequest;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.dto.TweetUpdateRequest;

import java.util.List;

public interface TweetService {

    TweetResponse createTweet(TweetCreateRequest tweetCreateRequest);

    List<TweetResponse> findByUserId(Long userId);

    TweetResponse findById(Long tweetId);


    TweetResponse updateTweet(Long tweetId,TweetUpdateRequest tweetUpdateRequest);

    List<TweetResponse> findByHashtagsName(String hashtagsName);

    void deleteTweet(Long tweetId);
}
