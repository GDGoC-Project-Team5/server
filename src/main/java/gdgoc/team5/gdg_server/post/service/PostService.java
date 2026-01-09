package gdgoc.team5.gdg_server.post.service;

import gdgoc.team5.gdg_server.post.domain.Post;
import gdgoc.team5.gdg_server.post.dto.PostRequestDto;
import gdgoc.team5.gdg_server.post.dto.PostResponseDto;
import gdgoc.team5.gdg_server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {

        Post post = new Post(
                requestDto.title(),
                requestDto.content(),
                requestDto.author()
        );

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));
        postRepository.delete(post);
    }

}