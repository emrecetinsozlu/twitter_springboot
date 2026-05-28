package com.twitter.demo.comment.controller;


import com.twitter.demo.comment.dto.CommentCreateRequest;
import com.twitter.demo.comment.dto.CommentResponse;
import com.twitter.demo.comment.dto.CommentUpdateRequest;
import com.twitter.demo.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponse createComment(
            @Valid
            @RequestBody CommentCreateRequest commentCreateRequest
    ) {
        return commentService.createComment(commentCreateRequest);
    }

    @PutMapping("/{id}")
    public CommentResponse updateComment(
            @PathVariable Long commentId,
            //@RequestParam Long userId,
            @Valid
            @RequestBody CommentUpdateRequest commentUpdateRequest
    ){
        return commentService.updateComment(commentUpdateRequest, commentId);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable Long id
           //@RequestParam Long userId
    ){
        commentService.deleteComment(id);
    }


}
