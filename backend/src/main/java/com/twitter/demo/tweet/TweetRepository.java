package com.twitter.demo.tweet;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByUserId(Long userId);
    List<Tweet> findByHashtagsName(String hashtagsName);
    @EntityGraph(attributePaths = {"user"})
    Page<Tweet> findAllByOrderByCreatedAtDesc(Pageable pageable);
    void deleteAllByUserId(Long userId);
}
