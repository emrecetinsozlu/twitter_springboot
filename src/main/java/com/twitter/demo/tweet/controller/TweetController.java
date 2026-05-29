package com.twitter.demo.tweet.controller;


import com.twitter.demo.tweet.dto.TweetCreateRequest;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.dto.TweetUpdateRequest;
import com.twitter.demo.tweet.service.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
    private final TweetService tweetService;

    @PostMapping
    public TweetResponse createTweet(@RequestBody @Valid TweetCreateRequest tweetCreateRequest) {
        return tweetService.createTweet(tweetCreateRequest);
    }

    //http://localhost:8080/findByUserId?userId=1
    @GetMapping("/findByUserId")
    public List<TweetResponse> findAllTweets(@RequestParam Long userId) {
        return tweetService.findByUserId(userId);
    }

    @GetMapping("/{tweetId}")
    public TweetResponse findTweetById(@PathVariable Long tweetId) {
        return tweetService.findById(tweetId);
    }


    // Artık kullanıcıyı url'den değil security contextten alacağız bunun için CurrentUserService yazdık.
    @PutMapping("/{tweetId}")
    public TweetResponse updateTweet(@PathVariable Long tweetId, @RequestBody @Valid TweetUpdateRequest tweetUpdateRequest) {
        return tweetService.updateTweet(tweetId, tweetUpdateRequest);
    }
    //GET /tweets/findByHashtag?name=java
    @GetMapping("/findByHashtag")
    public List<TweetResponse> findByHashtag(@RequestParam String name) {
        return tweetService.findByHashtagsName(name);
    }

    //DELETE /tweets/1?userId=5
    //Fakat kullanıcı bilgisini RequestParam ile almak güvenli değil artık SecurityContext'ten alacağız
    @DeleteMapping("{tweetId}")
    public void deleteTweet(@PathVariable Long tweetId, @RequestParam Long userId) {
        tweetService.deleteTweet(tweetId);

    }
}
