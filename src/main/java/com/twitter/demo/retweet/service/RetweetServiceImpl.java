package com.twitter.demo.retweet.service;


import com.twitter.demo.exception.ForbiddenException;
import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.retweet.Retweet;
import com.twitter.demo.retweet.RetweetRepository;
import com.twitter.demo.retweet.dto.RetweetCreateRequest;
import com.twitter.demo.retweet.dto.RetweetMapper;
import com.twitter.demo.retweet.dto.RetweetResponse;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public RetweetResponse retweet(RetweetCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.userId()
                ));

        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tweet not found with id: " + request.tweetId()
                ));

        boolean alreadyRetweeted = retweetRepository.existsByUserIdAndTweetId(
                request.userId(),
                request.tweetId()
        );

        if (alreadyRetweeted) {
            throw new RuntimeException("User already retweeted this tweet");
        }

        Retweet retweet = Retweet.builder()
                .user(user)
                .tweet(tweet)
                .build();

        Retweet savedRetweet = retweetRepository.save(retweet);

        return RetweetMapper.toResponse(savedRetweet);
    }

    @Override
    @Transactional
    public void deleteRetweet(Long retweetId, Long userId) {
        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Retweet not found with id: " + retweetId
                ));

        if (!retweet.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to delete this retweet");
        }

        retweetRepository.delete(retweet);
    }
}
