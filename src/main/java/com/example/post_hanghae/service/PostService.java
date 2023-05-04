package com.example.post_hanghae.service;

import com.example.post_hanghae.dto.*;
import com.example.post_hanghae.entity.Post;
import com.example.post_hanghae.entity.User;
import com.example.post_hanghae.entity.UserRoleEnum;
import com.example.post_hanghae.jwt.JwtUtil;
import com.example.post_hanghae.repository.CommentRepository;
import com.example.post_hanghae.repository.PostRepository;
import com.example.post_hanghae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor //fianl이 붙거나 @NotNull이 붙은 필드의 생성자를 자동 생성
public class PostService {

    public static final String SUBJECT_KEY = "sub";
    public static final String AUTHORIZATION_KEY = "auth";
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    //@Autowired 를 안쓰는 이유 -> private "final"을 꼭 써야하기 때문!(안전)
    // 게시글 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername()));

        return new PostResponseDto(post);
    }

    // 전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<AllResponseDto> getPosts() {
        List<AllResponseDto> allResponseDto = new ArrayList<>();
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();

        for (Post post : posts) {
            List<CommentResponseDto> comments;
            comments = commentRepository.findCommentsByPostId(post.getId());
            Long postLikeCnt = postLikeRepository.countPlike(post.getId());

            allResponseDto.add(new AllResponseDto(post, postLikeCnt, comments));
        }

        return allResponseDto;
    }

    // 선택한 게시글 조회
    @Transactional(readOnly = true)
    public AllResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NullPointerException("존재하지 않는 게시글입니다."));

        List<CommentResponseDto> comments = commentRepository.findCommentsByPostId(id);
        Long postLikeCnt = postLikeRepository.countPlike(id);

        return new AllResponseDto(post, postLikeCnt, comments);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto postRequestDto, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        UserRoleEnum userRoleEnum = user.getRole();

        // RoleEnum과 username을 비교
        // StringUtils 찾아보기
        if (StringUtils.equals(userRoleEnum, UserRoleEnum.USER.name())) {
            if (!StringUtils.equals(post.getUsername(), user.getUsername())) {
                throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
            } else {
                post.update(postRequestDto);
                return new PostResponseDto(post);
            }
        }
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }

    // 게시글 삭제
    @Transactional
    public MsgResponseDto deleteAll(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );

        UserRoleEnum userRoleEnum = user.getRole();

        if (StringUtils.equals(userRoleEnum, UserRoleEnum.USER.name())) {
            if (!StringUtils.equals(post.getUsername(), user.getUsername())) {
                return new MsgResponseDto("아이디가 같지 않습니다.", HttpStatusCode.valueOf(400));
            } else {
                commentRepository.deleteByPost_Id(post.getId()); // 게시물 삭제시 댓글 같이 삭제
                postRepository.delete(post);
                return new MsgResponseDto("삭제 성공", HttpStatusCode.valueOf(200));
            }
        }
        commentRepository.deleteByPost_Id(post.getId()); // 게시물 삭제시 댓글 같이 삭제
        postRepository.delete(post);
        return new MsgResponseDto("삭제 성공", HttpStatusCode.valueOf(200));
    }


    // 게시글 좋아요
    @Transactional
    public MsgResponseDto savePostLike(Long id, User user) {

        postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Long likeCnt = postLikeRepository.CntByPostIdAndUsername(id, user.getUsername());

        // 만약 테이블 속에서 특정 유저가 특정 post에 좋아요를 눌렀는지 확인하는 개수를 확인해서 0이면은 테이블에 데이터를 새로 넣어주고
        // 1개가 존재한다면 plike의 참 거짓을 판단해서 반대로 바꿔주는 과정을 조건문을 통해 작성하세요
        // 바꿔주는 과정은 update 즉 memo update 과정과 동일해요 참고해서 내가 쓴 글 흐름대로 if 문 작성을 생각해 보면 이해할 거예요
        // 업데이트 부분이 이해가 안되면 테이블 상에서 존재하는 데이터를 삭제하고 save를 다시 하는 방법도 고려해보면 좋아요
        // 공부하세요!!

        if (likeCnt == 0) {
            PostLike postLike = new PostLike(id, user.getUsername(), true);
            postLikeRepository.save(postLike);
            return new MsgResponseDto("좋아요", HttpStatusCode.valueOf(200));
        } else {
            PostLike postLike = postLikeRepository.findByPostIdAndUsername(id, user.getUsername());
            if (postLike.isPLike()) { //boolean 타입이기 때문에 get이 아니라 is로
                postLike.update(false);

                return new MsgResponseDto("좋아요 취소", HttpStatusCode.valueOf(200));
            } else {
                postLike.update(true);
                return new MsgResponseDto("좋아요", HttpStatusCode.valueOf(200));
            }
        }
    }
}