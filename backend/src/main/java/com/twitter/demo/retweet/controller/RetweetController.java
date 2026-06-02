package com.twitter.demo.retweet.controller;


import com.twitter.demo.retweet.dto.RetweetCreateRequest;
import com.twitter.demo.retweet.dto.RetweetResponse;
import com.twitter.demo.retweet.service.RetweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping
    public RetweetResponse retweet(
            @Valid @RequestBody RetweetCreateRequest request
    ) {
        return retweetService.retweet(request);
    }

    @DeleteMapping("/{id}")
    public void deleteRetweet(
            @PathVariable Long id
            //@RequestParam Long userId
    ) {
        retweetService.deleteRetweet(id);
    }
}
