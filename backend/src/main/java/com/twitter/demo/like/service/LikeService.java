package com.twitter.demo.like.service;

import com.twitter.demo.like.dto.LikeCreateRequest;
import com.twitter.demo.like.dto.LikeResponse;
import com.twitter.demo.like.dto.LikedUserDTO;

import java.util.List;

public interface LikeService {
    LikeResponse likeTweet(LikeCreateRequest request);

    void dislikeTweet(LikeCreateRequest request);

    void deleteAllByUserId(Long userId);


    /*
    Long countByTweetId(Long tweetId);

    List<LikedUserDTO> getLikedUsers(Long tweetId);

     */
}
