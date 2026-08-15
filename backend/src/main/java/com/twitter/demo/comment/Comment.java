package com.twitter.demo.comment;


import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

/*
Bir Tweet’in birçok Comment’i olabilir.
Bir User birçok Comment yazabilir.
Ama her Comment sadece bir Tweet’e ve bir User’a bağlıdır.
 */

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment content cannot be blank")
    @Size(min=1, max=200, message = "Comment content must be between 1 and 200 characters")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column(updatable = false,  nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
