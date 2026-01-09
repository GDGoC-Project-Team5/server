package gdgoc.team5.gdg_server.post.controller.response;

import java.time.LocalDateTime;

import gdgoc.team5.gdg_server.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 목록 조회 응답 (리스트용)")
public record PostListResponseDto(
	@Schema(description = "게시글 ID", example = "1")
	Long postId,

	@Schema(description = "조회수", example = "42")
	Long views,

	@Schema(description = "게시글 제목", example = "GDG 정기 모임 공지")
	String title,

	@Schema(description = "작성자 실명", example = "홍길동")
	String realName,

	@Schema(description = "첨부파일 존재 여부 (현재 항상 false, File 컨트롤러에서 구현 예정)", example = "false")
	Boolean hasFile,

	@Schema(description = "게시글 생성일시", example = "2024-01-10T14:30:00")
	LocalDateTime createdAt
) {
	public static PostListResponseDto fromDomain(Post post, Boolean hasFile, String realName) {
		return new PostListResponseDto(
			post.getId(),
			post.getViews(),
			post.getTitle(),
			realName,
			hasFile,
			post.getCreatedAt()
		);
	}
}
