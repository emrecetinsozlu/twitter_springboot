package com.twitter.demo.like.dto;

import java.time.LocalDateTime;

public record LikeResponse(
        Long id,
        Long tweetId,
        Long userId,
        String username,
        LocalDateTime createdAt
) {
}
