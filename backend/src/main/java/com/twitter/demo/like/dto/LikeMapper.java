package com.twitter.demo.like.dto;

import com.twitter.demo.like.Like;

public class LikeMapper {

    public static LikeResponse toResponse(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getTweet().getId(),
                like.getUser().getId(),
                like.getUser().getUsername(),
                like.getCreatedAt()
        );
    }
}
