package com.twitter.demo.tweet.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TweetCreateRequest (
        @NotBlank(message = "tweet boş olamaz")
        @Size(min = 1, max = 250, message = "250 karakterden fazla olamaz")
        String content,
        //Şimdilik security hazır olmadığı için userId request’ten alıyoruz. JWT gelince bunu token’dan alacağız.
        //@NotNull(message = "userId boş olamaz") Long userId
        List<String> hashtagNames // hashtagname null veya boş olabilir yani hashtag verilmemiş olabilir.
){}

/*
MultipartFile TweetCreateRequest içine koyulmaz. Image burda verilmiyor yani
 */