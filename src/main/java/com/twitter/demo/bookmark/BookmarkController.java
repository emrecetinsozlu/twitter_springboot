package com.twitter.demo.bookmark;


import com.twitter.demo.tweet.dto.TweetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
Hibernate ilişkileri tarafında artık güçlü örneklerimiz var:
Hashtag → Tweet ↔ Hashtag ManyToMany + ara tablo
Bookmark → User ↔ Tweet ManyToMany + ara tablo
Like/Retweet → ara entity yaklaşımı
 */

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;
    @PostMapping("/{tweetId}")
    public void addBookmark(@PathVariable("tweetId") long tweetId) {
        bookmarkService.addBookmark(tweetId);
    }
    @DeleteMapping("/{tweetId}")
    public void removeBookmark(@PathVariable("tweetId") long tweetId) {
        bookmarkService.removeBookmark(tweetId);
    }
    @GetMapping
    public List<TweetResponse> getMyBookmarks() {
        return bookmarkService.getMyBookmarks();
    }
}
