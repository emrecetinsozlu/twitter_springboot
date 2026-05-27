package com.twitter.demo.retweet.service;

import com.twitter.demo.retweet.dto.RetweetCreateRequest;
import com.twitter.demo.retweet.dto.RetweetResponse;

public interface RetweetService {

    RetweetResponse retweet(RetweetCreateRequest request);

    void deleteRetweet(Long retweetId, Long userId);


}
