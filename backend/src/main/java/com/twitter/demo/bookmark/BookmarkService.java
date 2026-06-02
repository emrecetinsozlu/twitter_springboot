package com.twitter.demo.bookmark;

import com.twitter.demo.tweet.dto.TweetResponse;

import java.util.List;

public interface BookmarkService {
    void addBookmark(Long tweetId);
    void removeBookmark(Long tweetId);
    List<TweetResponse> getMyBookmarks();
}
