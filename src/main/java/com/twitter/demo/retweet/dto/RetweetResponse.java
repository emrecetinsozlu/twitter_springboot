package com.twitter.demo.retweet.dto;

import java.time.LocalDateTime;

public record RetweetResponse(
        Long id,
        Long tweetId,
        Long userId,
        String username,
        LocalDateTime createdAt
) {
}
