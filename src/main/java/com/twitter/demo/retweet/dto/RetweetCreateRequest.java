package com.twitter.demo.retweet.dto;

import jakarta.validation.constraints.NotNull;

public record RetweetCreateRequest(
        @NotNull(message = "Tweet id is required")
        Long tweetId,

        @NotNull(message = "User id is required")
        Long userId
) {
}
