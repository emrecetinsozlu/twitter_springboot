package com.twitter.demo.bookmark;

import com.twitter.demo.exception.BadRequestException;
import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.security.CurrentUserService;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.tweet.dto.TweetMapper;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.service.TweetService;
import com.twitter.demo.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final TweetRepository tweetRepository;
    private final CurrentUserService currentUserService;
    private final TweetService tweetService;


    @Override
    @Transactional
    public void addBookmark(Long tweetId) {
        System.out.println("add bookmarked");
        User currentUser = currentUserService.getCurrentUser();
        System.out.println("current user: " + currentUser.getUsername() + " id: " + tweetId);
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadı"));
        if(currentUser.getBookmarkedTweets().contains(tweet)){
            throw new BadRequestException("Bu tweet zaten bookmarked a eklenmiş");
        }
        currentUser.addBookmark(tweet);

    }

    @Override
    @Transactional
    public void removeBookmark(Long tweetId) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadı"));
        if(!currentUser.getBookmarkedTweets().contains(tweet)){
            throw new BadRequestException("Bu tweet zaten bookmarked a eklenmemiş");
        }
        currentUser.removeBookmark(tweet);
    }

    @Override
    public List<TweetResponse> getMyBookmarks() {
        User currentUser = currentUserService.getCurrentUser();
        return currentUser.getBookmarkedTweets()
                .stream()
                .map(tweetService::buildTweetResponse)
                .toList();

    }
}
