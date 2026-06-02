package com.twitter.demo.user;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.twitter.demo.tweet.Tweet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "twitter")
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 1, max = 20)
    @Column(nullable = false, length = 20, unique = true)
    private String username;
    @Email
    @NotBlank
    @Size(min = 1, max = 100)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    /*

    User birçok tweet’i bookmark edebilir.
    Tweet birçok user tarafından bookmark edilebilir.
     */
    //Aksiyon User içerisinde olduğu için owningside olarak userı belirledik o yüzden jointable ı burda belirticez helper methodlar da burda olacak
    //aksiyondan kasit bookmarka tweet ekleme çıkartma gibi
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id"),
            name = "user_bookmarks"
    )
    @JsonManagedReference("user_bookmarks")
    private Set<Tweet> bookmarkedTweets = new HashSet<>();

    public void addBookmark(Tweet tweet) {
        bookmarkedTweets.add(tweet);
        tweet.getBookmarkedUsers().add(this);
    }
    public void removeBookmark(Tweet tweet) {
        bookmarkedTweets.remove(tweet);
        tweet.getBookmarkedUsers().remove(this);
    }

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }



}
