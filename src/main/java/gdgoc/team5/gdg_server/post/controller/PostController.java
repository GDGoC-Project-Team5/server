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
import gdgoc.team5.gdg_server.common.controller.security.AdminOnly;
import gdgoc.team5.gdg_server.post.controller.request.PostRequestDto;
import gdgoc.team5.gdg_server.post.controller.response.PostListResponseDto;
import gdgoc.team5.gdg_server.post.controller.response.PostResponseDto;
import gdgoc.team5.gdg_server.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	@Operation(
		summary = "게시글 작성",
		description = "새로운 게시글을 작성합니다. 파일 업로드 기능은 File 컨트롤러에 구현 예정입니다. **관리자 권한이 필요합니다.**"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "403", description = "관리자 권한 필요")
	})
	@AdminOnly
	@PostMapping("")
	public ResponseEntity<Void> createPost(
		@RequestBody @Valid PostRequestDto requestDto,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		postService.createPost(requestDto, tokenInfo);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "게시글 상세 조회",
		description = "게시글 ID로 단일 게시글의 상세 정보를 조회합니다. 조회 시 조회수가 1 증가합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "게시글 조회 성공",
			content = @Content(schema = @Schema(implementation = PostResponseDto.class))
		),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	@GetMapping("/{postId}")
	public PostResponseDto getPostById(
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@PathVariable(name = "postId") Long postId,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		return postService.getPostById(postId);
	}

	@Operation(
		summary = "게시글 목록 조회",
		description = "게시글 목록을 페이지네이션하여 조회합니다. 최신순으로 정렬됩니다. " +
			"제목으로 검색할 수 있습니다. " +
			"※ sort 파라미터는 무시하고, page와 size만 사용 가능합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "게시글 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = Page.class))
		)
	})
	@GetMapping("")
	public Page<PostListResponseDto> getAllPosts(
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@Parameter(description = "검색할 제목 (선택사항)", example = "공지")
		@RequestParam(required = false) String title
	) {
		return postService.getAllPosts(pageable, title);
	}

	@Operation(
		summary = "게시글 삭제",
		description = "게시글 ID로 게시글을 삭제합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@PathVariable(name = "postId") Long postId
	) {
		postService.deletePost(postId);
		return ResponseEntity.ok().build();
	}
}
