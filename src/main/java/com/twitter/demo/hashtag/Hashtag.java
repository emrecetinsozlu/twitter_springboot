package com.twitter.demo.hashtag;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.twitter.demo.tweet.Tweet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hashtags", schema = "twitter")
@Builder
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    //bu ilişki bidirectional, çünkü Hashtag hem Tweet'e referans verir hem de Tweet Hashtag'e referans verir. Bu sayede her iki taraftan da ilişkiye erişilebilir.
    // mappedby dediğimizde bu; ben veritabanındaki ara tabloyu veya kuralları bilmiyorum. Git owning side a bak orda benim tipimde bir değişken (List/Set) var. İlişkinin tüm veri tabanı, ara tablo ve sütun ayarları zaten o değişkenin üzerindeki @JoinTable içinde yazılı. Beni o değişkene bağla."
    @ManyToMany(mappedBy = "hashtags")
    @JsonBackReference("tweet_hashtags")
    @Builder.Default
    // eğer builder.default kullanmazsam tweet i builder patterni ile oluştururken tweets i girmezsem null gelir. Kısaca builder.default başlangıçta atadığınd default dereğerlerin builder da atanmasa bile ezilip boş olarak atanmasının önüne geçer
    private Set<Tweet> tweets = new HashSet<>();
}
