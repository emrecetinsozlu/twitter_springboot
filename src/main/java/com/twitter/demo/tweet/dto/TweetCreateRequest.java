package com.twitter.demo.tweet.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TweetCreateRequest (
        @NotBlank(message = "tweet boş olamaz")
        @Size(min = 1, max = 250, message = "280 karakterden fazla olamaz")
        String content,
        //Şimdilik security hazır olmadığı için userId request’ten alıyoruz. JWT gelince bunu token’dan alacağız.
        @NotNull
        Long userId
){}
