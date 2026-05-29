package com.twitter.demo.tweet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TweetUpdateRequest(

        @NotBlank(message = "Tweet content cannot be blank")
        @Size(max = 280, message = "Tweet content cannot exceed 280 characters")
        String content,
        List<String> hashtagNames
) {
}
