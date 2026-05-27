package com.twitter.demo.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @NotBlank(message = "Comment content cannot be blank")
        @Size(max = 280, message = "Comment content cannot exceed 280 characters")
        String content
) {
}
