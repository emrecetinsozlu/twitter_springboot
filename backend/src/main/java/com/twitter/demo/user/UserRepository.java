package com.twitter.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByIdAndBookmarkedTweets_id(Long id, Long tweetId);

    //delete methodu jpa tarafından otomatik olarak sağlanır, bu yüzden burada tanımlamaya gerek yoktur.
}
