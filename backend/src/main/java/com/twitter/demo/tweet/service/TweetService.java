package com.twitter.demo.tweet.service;

import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.dto.PagedResponse;
import com.twitter.demo.tweet.dto.TweetCreateRequest;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.dto.TweetUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TweetService {
    /*
    Artık image da gönderebileceğiz.
     */
    TweetResponse createTweet(TweetCreateRequest tweetCreateRequest, MultipartFile image);

    List<TweetResponse> findByUserId(Long userId);

    TweetResponse findById(Long tweetId);

    PagedResponse<TweetResponse> getAllTweets(int page, int size);

    TweetResponse updateTweet(Long tweetId,TweetUpdateRequest tweetUpdateRequest);

    List<TweetResponse> findByHashtagsName(String hashtagsName);

    void deleteTweet(Long tweetId);

    TweetResponse buildTweetResponse(Tweet tweet);
}
