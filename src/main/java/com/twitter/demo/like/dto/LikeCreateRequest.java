package com.twitter.demo.like.dto;

import jakarta.validation.constraints.NotNull;

public record LikeCreateRequest(

        @NotNull(message = "Tweet id is required")
        Long tweetId,

        @NotNull(message = "User id is required")
        Long userId
) {
}
