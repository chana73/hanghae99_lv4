package com.example.post_hanghae.controller;

import com.example.post_hanghae.dto.CommentRequestDto;
import com.example.post_hanghae.dto.CommentResponseDto;
import com.example.post_hanghae.dto.MsgResponseDto;
import com.example.post_hanghae.security.UserDetailsImpl;
import com.example.post_hanghae.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    // 댓글 등록
    @PostMapping("/post/{id}/comment")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(id, commentRequestDto, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/put/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, commentRequestDto, userDetails.getUser());
    }


    // 댓글 삭제
    @DeleteMapping("/delete/comment/{id}")
    public MsgResponseDto deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    // 댓글 좋아요
//    @PostMapping("/post/comment/{id}/like")
//    public MsgResponseDto createCommentLike(@PathVariable Long id, HttpServletRequest request) {
//        return commentService.createCommentLike(id, request);
//    }

}
