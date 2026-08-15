package com.twitter.demo.tweet.dto;

import com.twitter.demo.tweet.Tweet;

public class TweetMapper {

    public static TweetResponse toTweetResponse(Tweet tweet, Long likeCount, Boolean liked, Boolean bookmarked) {
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername(),
                likeCount,
                liked,
                bookmarked,
                tweet.getHashtags()
                        .stream()
                        .map(hashtag -> hashtag.getName())
                        .toList(),
                tweet.getImageUrl(),
                tweet.getCreatedAt(),
                tweet.getUpdatedAt()
        );
    }
}
