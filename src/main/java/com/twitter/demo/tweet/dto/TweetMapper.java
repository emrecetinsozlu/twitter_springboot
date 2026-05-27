package com.twitter.demo.tweet.dto;

import com.twitter.demo.tweet.Tweet;

public class TweetMapper {

    public static TweetResponse toTweetResponse(Tweet tweet) {
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername(),
                tweet.getCreatedAt(),
                tweet.getUpdatedAt()
        );
    }
}
