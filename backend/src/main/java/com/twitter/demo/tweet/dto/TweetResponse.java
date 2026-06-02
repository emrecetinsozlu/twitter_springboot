package com.twitter.demo.tweet.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TweetResponse(
        Long id,
        String content,
        Long userId,
        String username,
        List<String> hashtags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
