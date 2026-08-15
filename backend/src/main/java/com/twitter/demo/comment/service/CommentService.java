package com.twitter.demo.comment.service;

import com.twitter.demo.comment.dto.CommentCreateRequest;
import com.twitter.demo.comment.dto.CommentResponse;
import com.twitter.demo.comment.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentCreateRequest commentCreateRequest);
    // Artık userId bilgisini token dan alacağız
    //CommentResponse updateComment(CommentUpdateRequest commentUpdateRequest, Long commentId, Long userId);
    CommentResponse updateComment(CommentUpdateRequest commentUpdateRequest, Long commentId);
    void deleteComment(Long commentId);

    List<CommentResponse> getCommentsByTweetId(Long tweetId);
}
