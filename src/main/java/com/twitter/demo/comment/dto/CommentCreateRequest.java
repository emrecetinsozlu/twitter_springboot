package com.twitter.demo.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank(message = "Comment content cannot be blank")
        @Size(max = 280, message = "Comment content cannot exceed 280 characters")
        String content,

        @NotNull(message = "Tweet id is required")
        Long tweetId
        //Artık kullanıcı bilgisini security context'ten alacağız.
        //bir kullanıcı login olduğunda jwt bir token generate ediyor ve bu token içerisinde kullanıcı bilgisi var.
        //@NotNull(message = "User id is required")
        //Long userId
) {
}
