package com.twitter.demo.like.repository;

import com.twitter.demo.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository  extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);

}
