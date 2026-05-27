package com.twitter.demo.comment.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        Long tweetId,
        Long userId,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
