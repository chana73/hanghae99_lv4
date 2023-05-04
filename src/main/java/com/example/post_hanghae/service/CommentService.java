package com.example.post_hanghae.service;

import com.example.post_hanghae.dto.*;
import com.example.post_hanghae.entity.*;
import com.example.post_hanghae.repository.CommentRepository;
import com.example.post_hanghae.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, User user) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        Comment comment = new Comment(commentRequestDto);

        comment.setPost(post);

        comment.setUsername(user.getUsername());

        commentRepository.saveAndFlush(comment);
        return new CommentResponseDto(comment);
    }

    // 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        UserRoleEnum userRoleEnum = user.getRole();


        if (StringUtils.equals(userRoleEnum, UserRoleEnum.USER.name())) {
            if(!StringUtils.equals(comment.getUsername(), user.getUsername())) {
                throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
            } else {
                comment.updatecomment(commentRequestDto);
                return new CommentResponseDto(comment);
            }
        }
        comment.updatecomment(commentRequestDto);
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public MsgResponseDto deleteComment(Long id, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        UserRoleEnum userRoleEnum = user.getRole();

        if(StringUtils.equals(userRoleEnum, UserRoleEnum.USER.name())) {
            if (!StringUtils.equals(comment.getUsername(), user.getUsername())) {
                return  new MsgResponseDto("아이디가 같지 않습니다.", HttpStatusCode.valueOf(400));
            } else {
                commentRepository.delete(comment);
                return new MsgResponseDto("댓글 삭제 성공", HttpStatusCode.valueOf(200));
            }
        }
        commentRepository.delete(comment);
        return new MsgResponseDto("댓글 삭제 성공", HttpStatusCode.valueOf(200));
    }


    // 댓글 좋아요
//    @Transactional
//    public MsgResponseDto createCommentLike(Long id, HttpServletRequest request) {
//        Comment comment = commentRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
//        );
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//        if (token == null) {
//            return null;
//        }
//        if (!jwtUtil.validateToken(token)) {
//            return new MsgResponseDto("토큰이 유효하지 않습니다.", HttpStatusCode.valueOf(400));
//        }
//        claims = jwtUtil.getUserInfoFromToken(token);
//        String username = claims.get(SUBJECT_KEY, String.class);
//
//        Long cLikeCnt = CommentLikeRepository.findByCommentIdAndUsername(id, user.getUsername());
//
//        CommentLike commentLike = new CommentLike(id, user.getUsername(), true);
//        if (cLikeCnt == 0) {
//            CommentLikeRepository.save(commentLike);
//        } else {
//            if (CommentLikeRepository.checkClike(id, user.getUsername())) {
//                CommentLikeRepository.delete(commentLike);
//                return new MsgResponseDto("좋아요 취소", HttpStatusCode.valueOf(200));
//            }
//        }
//
//        return new MsgResponseDto("좋아요", HttpStatusCode.valueOf(200));
//  }
//
}
