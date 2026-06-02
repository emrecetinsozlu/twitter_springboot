package com.twitter.demo.tweet.service;

import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.exception.UserNotFoundException;
import com.twitter.demo.hashtag.Hashtag;
import com.twitter.demo.hashtag.HashtagRepository;
import com.twitter.demo.security.CurrentUserService;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.tweet.dto.TweetCreateRequest;
import com.twitter.demo.tweet.dto.TweetMapper;
import com.twitter.demo.tweet.dto.TweetResponse;
import com.twitter.demo.tweet.dto.TweetUpdateRequest;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.service.UnknownServiceException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service

public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    private final HashtagRepository hashtagRepository;

    public TweetServiceImpl(TweetRepository tweetRepository,  UserRepository userRepository, CurrentUserService currentUserService,  HashtagRepository hashtagRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.hashtagRepository = hashtagRepository;
    }

    private String normalizeHashtag(String hashtagName) {
        return hashtagName
                .trim()
                .replace("#", "")
                .toLowerCase();
    }

    @Override
    @Transactional
    public TweetResponse createTweet(TweetCreateRequest tweetCreateRequest) {
        User currentUser =  currentUserService.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User bulunamadı"));
        Tweet tweet = Tweet.builder()
                .content(tweetCreateRequest.content())
                .user(user)
                .build();

        //Eğer createRequestte hashtag name belirtilmişse onları normalize edip tweet'e ekleyelim #springboot gibi
        if(tweetCreateRequest.hashtagNames() != null && !tweetCreateRequest.hashtagNames().isEmpty()){
            //istek içindeki tüm hashtag name stringleri al
            tweetCreateRequest.hashtagNames().stream()
                    .map(this::normalizeHashtag)
                    .distinct()
                    // her bir hashtag in db deki karşılığını çek ve ondan bir hashtag entitysi oluştur ve son oalrak tweet içindeki listeye koy. addhashtag hem tweet içindeki hashtags listesine hem de hashtag içindeki tweets listesine ekleme yapar
                    .forEach(hashtagName -> {
                        // Process each normalized hashtag
                        Hashtag hashtag = hashtagRepository.findByName(hashtagName)
                                .orElseGet(() ->
                                    Hashtag.builder()
                                            .name(hashtagName)
                                            .build()
                                );
                        tweet.addHashtag(hashtag);
                    });

        }

        Tweet createdTweet = tweetRepository.save(tweet);

        return TweetMapper.toTweetResponse(createdTweet);
    }

    @Override
    @Transactional
    public List<TweetResponse> findByUserId(Long userId) {
        return tweetRepository.findByUserId(userId).stream().map(TweetMapper::toTweetResponse).toList();

    }

    @Override
    public TweetResponse findById(Long tweetId) {
        return tweetRepository.findById(tweetId).map(TweetMapper::toTweetResponse).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

    }



    //HASHTAGLERLE UPDATE İÇİN HELPER METHOD

    private void updateTweetHashtags(Tweet tweet, List<String> hashtagNames) {
        /*
        hashtagNames == null → hashtagleri değiştirme
        hashtagNames == []   → tüm hashtagleri kaldır
        hashtagNames dolu    → eski hashtagleri bu listeyle değiştir
         */

        if (hashtagNames == null) {
            return;
        }

        Set<Hashtag> oldHashtags = new HashSet<>(tweet.getHashtags());

        oldHashtags.forEach(tweet::removeHashtag);

        hashtagNames.stream()
                .map(this::normalizeHashtag)
                .filter(name -> !name.isBlank())
                .distinct()
                .forEach(name -> {
                    Hashtag hashtag = hashtagRepository.findByName(name)
                            .orElseGet(() -> Hashtag.builder()
                                    .name(name)
                                    .build());

                    tweet.addHashtag(hashtag);
                });
    }

    // Kullanıcı bilgisini artık userId olarak url requestparam olarak değil de securitycontextten alacağız
    @Override
    public TweetResponse updateTweet(Long tweetId, TweetUpdateRequest tweetUpdateRequest) {

        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
        //Aktif kullanıcı bilgisini security contextten çekmiş olduk.
        User currentUser = currentUserService.getCurrentUser();
        if (!tweet.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("User is not authorized to update this tweet");
        }
        tweet.setContent(tweetUpdateRequest.content());
        updateTweetHashtags(tweet, tweetUpdateRequest.hashtagNames());


        //@Transcational kullanırsam save ile kaydetmeme gerek yok otomatikman kendi kaydederdi
        Tweet updatedTweet = tweetRepository.save(tweet);
        return TweetMapper.toTweetResponse(updatedTweet);


    }

    @Override
    @Transactional
    public List<TweetResponse> findByHashtagsName(String hashtagsName) {
        String normalizedHashtag = normalizeHashtag(hashtagsName);
        return tweetRepository.findByHashtagsName(normalizedHashtag).stream()
                .map(TweetMapper::toTweetResponse)
                .toList();

    }

    @Override
    public void deleteTweet(Long tweetId) {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new RuntimeException("Tweet not found"));
        User currentUser = currentUserService.getCurrentUser();
        if (!tweet.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("User is not authorized to delete this tweet");
        }
        tweetRepository.delete(tweet);
    }
}
