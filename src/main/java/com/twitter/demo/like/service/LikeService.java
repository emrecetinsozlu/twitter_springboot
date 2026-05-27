package com.twitter.demo.like.service;

import com.twitter.demo.like.dto.LikeCreateRequest;
import com.twitter.demo.like.dto.LikeResponse;

public interface LikeService {
    LikeResponse likeTweet(LikeCreateRequest request);

    void dislikeTweet(LikeCreateRequest request);
}
