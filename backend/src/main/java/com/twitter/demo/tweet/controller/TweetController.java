package com.twitter.demo.tweet.controller;


import com.twitter.demo.tweet.dto.PagedResponse;
import com.twitter.demo.tweet.dto.TweetCreateRequest;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.dto.TweetUpdateRequest;
import com.twitter.demo.tweet.service.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
public class TweetController {
    private final TweetService tweetService;
    /*
    @PostMapping
    public TweetResponse createTweet(@RequestBody @Valid TweetCreateRequest tweetCreateRequest) {
        return tweetService.createTweet(tweetCreateRequest);
    }
    */

   // Content-Type: multipart/form-data olduğu için artık @ModelAttribute kullanacağız. Ve image'ı da MultipartFile olarak alacağız.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TweetResponse createTweet(
            @Valid @ModelAttribute TweetCreateRequest request,
            @RequestParam(required = false) MultipartFile image
    ) {
        return tweetService.createTweet(request, image);
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

    //GET http://localhost:3000/tweet?page=0&size=10
    @GetMapping
    public ResponseEntity<PagedResponse<TweetResponse>> getAllTweets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (size > 30) {
            size = 30;
        }

        PagedResponse<TweetResponse> response = tweetService.getAllTweets(page, size);
        return ResponseEntity.ok(response);
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
    @DeleteMapping("/{tweetId}")
    public void deleteTweet(@PathVariable Long tweetId, @RequestParam Long userId) {
        tweetService.deleteTweet(tweetId);

    }
}
