package com.twitter.demo.like;


import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @UniqueConstraint, veri tabanındaki bir tabloda belirli bir sütunun veya birden fazla sütunun kombinasyonunun benzersiz (unique) olmasını sağlamak için kullanılır.
@Table(name = "likes", schema = "twitter", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tweet_id", "user_id"})
})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
