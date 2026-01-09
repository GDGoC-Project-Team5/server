package gdgoc.team5.gdg_server.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.common.controller.argresolver.AuthenticatedToken;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import gdgoc.team5.gdg_server.post.controller.request.PostRequestDto;
import gdgoc.team5.gdg_server.post.controller.response.PostListResponseDto;
import gdgoc.team5.gdg_server.post.controller.response.PostResponseDto;
import gdgoc.team5.gdg_server.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	@PostMapping("")
	public ResponseEntity<Void> createPost(
		@RequestBody @Valid PostRequestDto requestDto,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		postService.createPost(requestDto, tokenInfo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{postId}")
	public PostResponseDto getPostById(
		@PathVariable(name = "postId") Long postId,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		return postService.getPostById(postId);
	}

	@GetMapping("")
	public Page<PostListResponseDto> getAllPosts(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) String title
	) {
		return postService.getAllPosts(pageable, title);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		postService.deletePost(id);
		return ResponseEntity.ok().build();
	}
}
