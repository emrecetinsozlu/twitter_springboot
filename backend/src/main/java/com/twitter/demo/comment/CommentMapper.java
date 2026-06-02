package com.twitter.demo.comment;

import com.twitter.demo.comment.dto.CommentResponse;

public class CommentMapper {

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTweet().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
