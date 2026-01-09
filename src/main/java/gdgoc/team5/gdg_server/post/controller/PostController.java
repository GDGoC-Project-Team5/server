package gdgoc.team5.gdg_server.post.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gdgoc.team5.gdg_server.common.controller.argresolver.AuthenticatedToken;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import gdgoc.team5.gdg_server.post.dto.PostRequestDto;
import gdgoc.team5.gdg_server.post.dto.PostResponseDto;
import gdgoc.team5.gdg_server.post.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PostResponseDto createPost(
		@RequestPart("post") PostRequestDto requestDto,
		@RequestPart(value = "file", required = false) MultipartFile file,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		return postService.createPost(requestDto, file);
	}

	@GetMapping("/{postId}")
	public PostResponseDto getPostById(
		@PathVariable(name = "postId") Long postId,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		return postService.getPostById(postId);
	}

	@GetMapping("")
	public List<PostResponseDto> getAllPosts() {
		return postService.getAllPosts();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id) {
		try {
			postService.deletePost(id);
			return ResponseEntity.ok("게시물이 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity
				.status(404)
				.body(e.getMessage());
		}
	}
}
