package com.twitter.demo.tweet;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.twitter.demo.hashtag.Hashtag;
import com.twitter.demo.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tweets", schema = "twitter")
@Builder
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "tweet boş olamaz")
    @Size(min = 1, max = 250)
    @Column(nullable = false, length = 250)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    //cascade all vermedik çünkü bir tweet silindiğinde hashtag in kendisnin de silinmesini istemiyoruz
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //tweetlere hashtag ekleyeceğiz yani aksiyon burda o yüzden owningside burası aratabloyu da burda oluşturacağız
    @JoinTable(name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    @JsonManagedReference("tweet_hashtags")
    @Builder.Default
    private Set<Hashtag> hashtags = new HashSet<>();

    //bir tweet bir çok kullanıcı tarafından bookmarklanabilir, bir kullanıcı da bir çok tweeti bookmarklayabilir. Bu yüzden bu ilişki de ManyToMany
    //bir tweet i kimler/hangi userlar bookmarklado
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "bookmarkedTweets")
    @JsonBackReference("user_bookmarks")

    private Set<User> bookmarkedUsers = new HashSet<>();
    /*
    Bookmark feature'ın asıl amacı yeni bir şey öğretmekten çok şunu pekiştirmekti:
    "ManyToMany kullanacağım durum"
    ile
    "Ara entity kullanacağım durum"
    arasındaki çizgi.
     */



    @Column(nullable = false)
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

    // hem memory hem de repository tarafında ilişkiyi yönetmek için yardımcı metodlar ekleyelim
    // tweet.getHashtags().add(hashtag); sadece bunu yapsaydık DB kaydı çoğu durumda oluşurdu ama memory’de hashtag.getTweets() tarafı güncel görünmezdi.
    //Biz bidirectional ilişkiyi doğru yönetmek için iki tarafı da güncelliyoruz:
    public void addHashtag(Hashtag hashtag) {
        this.hashtags.add(hashtag);
        hashtag.getTweets().add(this);
    }

    public void removeHashtag(Hashtag hashtag) {
        this.hashtags.remove(hashtag);
        hashtag.getTweets().remove(this);
    }
}
