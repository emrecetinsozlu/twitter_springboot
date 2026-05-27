package com.twitter.demo.comment.service;

import com.twitter.demo.comment.dto.CommentCreateRequest;
import com.twitter.demo.comment.dto.CommentResponse;
import com.twitter.demo.comment.dto.CommentUpdateRequest;

public interface CommentService {
    CommentResponse createComment(CommentCreateRequest commentCreateRequest);
    CommentResponse updateComment(CommentUpdateRequest commentUpdateRequest, Long commentId, Long userId);
    void deleteComment(Long commentId, Long userId);
}
