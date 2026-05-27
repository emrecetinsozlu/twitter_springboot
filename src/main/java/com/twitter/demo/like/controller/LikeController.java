package com.twitter.demo.like.controller;


import com.twitter.demo.like.Like;
import com.twitter.demo.like.dto.LikeCreateRequest;
import com.twitter.demo.like.dto.LikeResponse;
import com.twitter.demo.like.repository.LikeRepository;
import com.twitter.demo.like.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like")
    public LikeResponse likeTweet(@Valid  @RequestBody LikeCreateRequest  likeCreateRequest) {
        return likeService.likeTweet(likeCreateRequest);
    }
    @PostMapping("/dislike")
    public void dislikeTweet(@Valid @RequestBody LikeCreateRequest  likeCreateRequest) {
        likeService.dislikeTweet(likeCreateRequest);
    }
}
