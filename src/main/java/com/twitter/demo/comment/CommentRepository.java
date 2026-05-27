package com.twitter.demo.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweetId(Long tweetId);
    List<Comment> findByUserId(Long userId);
}
