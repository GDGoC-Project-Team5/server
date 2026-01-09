package gdgoc.team5.gdg_server.post;

import gdgoc.team5.gdg_server.post.dto.PostRequestDto;
import gdgoc.team5.gdg_server.post.dto.PostResponseDto;
import gdgoc.team5.gdg_server.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostMockController {

    private final PostService postService;

    @PutMapping("/posts/write")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @GetMapping("/posts/getPost/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/posts/getAllPost")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @DeleteMapping("/posts/delete{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
