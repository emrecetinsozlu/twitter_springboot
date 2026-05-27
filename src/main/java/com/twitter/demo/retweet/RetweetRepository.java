package com.twitter.demo.retweet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
        //aynı kullanıcı aynı tweeti daha önce retweet etmiş mi
        boolean existsByUserIdAndTweetId(Long userId, Long tweetId);

        Optional<Retweet> findByUserIdAndTweetId(Long userId, Long tweetId);

}
