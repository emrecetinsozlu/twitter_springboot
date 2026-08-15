package com.twitter.demo.tweet.service;

import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.exception.UserNotFoundException;
import com.twitter.demo.hashtag.Hashtag;
import com.twitter.demo.hashtag.HashtagRepository;
import com.twitter.demo.like.repository.LikeRepository;
import com.twitter.demo.media.ImageUploadResult;
import com.twitter.demo.media.ImageUploadService;
import com.twitter.demo.security.CurrentUserService;
import com.twitter.demo.tweet.Tweet;
import com.twitter.demo.tweet.TweetRepository;
import com.twitter.demo.tweet.dto.*;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.service.UnknownServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service

public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final LikeRepository likeRepository;
    private final HashtagRepository hashtagRepository;
    private final ImageUploadService imageUploadService;

    public TweetServiceImpl(TweetRepository tweetRepository,  UserRepository userRepository, CurrentUserService currentUserService, ImageUploadService imageUploadService,  LikeRepository likeRepository, HashtagRepository hashtagRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.imageUploadService = imageUploadService;
        this.hashtagRepository = hashtagRepository;
        this.likeRepository = likeRepository;
    }

    private String normalizeHashtag(String hashtagName) {
        return hashtagName
                .trim()
                .replace("#", "")
                .toLowerCase();
    }


    /*
    TweetCreateRequest → content
MultipartFile image → ayrı parametre
Tweet entity → content + imageUrl + imagePublicId
     */

    @Override
    @Transactional
    public TweetResponse createTweet(TweetCreateRequest tweetCreateRequest, MultipartFile image) {
        User currentUser =  currentUserService.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User bulunamadı"));
        Tweet tweet = Tweet.builder()
                .content(tweetCreateRequest.content())
                .user(user)
                .build();
        if (image != null && !image.isEmpty()) {
            ImageUploadResult imageUploadResult = imageUploadService.uploadImage(image);

            tweet.setImageUrl(imageUploadResult.imageUrl());
            tweet.setImagePublicId(imageUploadResult.publicId());
        }


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

        //return TweetMapper.toTweetResponse(createdTweet,0L,false,false);
        return buildTweetResponse(createdTweet);
    }

    @Override
    @Transactional
    public List<TweetResponse> findByUserId(Long userId) {
        return tweetRepository.findByUserId(userId).stream().map(
                tweet -> {
                    Long likeCount = likeRepository.countByTweetId(tweet.getId());
                    //return TweetMapper.toTweetResponse(tweet,likeCount,false,false);
                    return buildTweetResponse(tweet);
                }
        ).toList(
        );

    }

    @Override
    public TweetResponse findById(Long tweetId) {
        Long likeCount = likeRepository.countByTweetId(tweetId);
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
       // return TweetMapper.toTweetResponse(tweet,likeCount,false,false);
       // return tweetRepository.findById(tweetId).map(TweetMapper::toTweetResponse).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
        return buildTweetResponse(tweet);
    }

    @Override
    @Transactional
    public PagedResponse<TweetResponse> getAllTweets(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Tweet> tweetPage = tweetRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<TweetResponse> tweetResponses = tweetPage
                .getContent()
                .stream()
                .map(this::buildTweetResponse)
                .toList();

        return new PagedResponse<>(
                tweetResponses,
                tweetPage.getNumber(),
                tweetPage.getSize(),
                tweetPage.getTotalElements(),
                tweetPage.getTotalPages(),
                tweetPage.isLast()
        );
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
        //Long likeCount = likeRepository.countByTweetId(tweetId);
        //return TweetMapper.toTweetResponse(updatedTweet,likeCount);
        return buildTweetResponse(updatedTweet);

    }

    @Override
    @Transactional
    public List<TweetResponse> findByHashtagsName(String hashtagsName) {
        String normalizedHashtag = normalizeHashtag(hashtagsName);
        return tweetRepository.findByHashtagsName(normalizedHashtag).stream()
                .map(
                        this::buildTweetResponse
                )
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


    /*
    Böylece login yokken currentUserService.getCurrentUser() hiç çağrılmaz, 404 oluşmaz. Login varsa aynı eski davranış devam eder.
Not: Bu çözüm çalışır ama daha temiz tasarım CurrentUserService içine getCurrentUserOrNull() veya getOptionalCurrentUser() koymaktır. Çünkü “giriş yapılmış mı?” bilgisinin yeri service olur, tweet response builder içinde Spring Security detayları yayılmaz.

     */


    @Override
    public TweetResponse buildTweetResponse(Tweet tweet) {
        Long likeCount = likeRepository.countByTweetId(tweet.getId());
        // 1. Önce mevcut kullanıcıyı güvenli bir şekilde almayı dene
        boolean isLiked = false;
        boolean isBookmarked = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {
            User currentUser = currentUserService.getCurrentUser();

            isLiked = likeRepository.existsByUserIdAndTweetId(currentUser.getId(), tweet.getId());
            isBookmarked = userRepository.existsByIdAndBookmarkedTweets_id(currentUser.getId(), tweet.getId());
        }
        //return TweetMapper.toTweetResponse(tweet, likeCount);
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername(),
                likeCount,
                isLiked,
                isBookmarked,
                tweet.getHashtags().stream().map(Hashtag::getName).toList(),
                tweet.getImageUrl(),
                tweet.getCreatedAt(),
                tweet.getUpdatedAt()
        );
    }
}
