package com.twitter.demo.tweet.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TweetResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Long likeCount,
        Boolean liked,
        Boolean bookmarked,
        List<String> hashtags,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
