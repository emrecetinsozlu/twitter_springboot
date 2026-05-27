package com.twitter.demo.retweet.dto;

import com.twitter.demo.retweet.Retweet;

public class RetweetMapper {

    public static RetweetResponse toResponse(Retweet retweet) {
        return new RetweetResponse(
                retweet.getId(),
                retweet.getTweet().getId(),
                retweet.getUser().getId(),
                retweet.getUser().getUsername(),
                retweet.getCreatedAt()
        );
    }

}
