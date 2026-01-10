package gdgoc.team5.gdg_server.post.controller.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import gdgoc.team5.gdg_server.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 상세 조회 응답")
public record PostResponseDto(
	@Schema(description = "게시글 ID", example = "1")
	Long postId,

	@Schema(description = "게시글 제목", example = "GDG 정기 모임 공지")
	String title,

	@Schema(description = "게시글 본문 내용", example = "다음 주 수요일 7시에 정기 모임이 있습니다.")
	String content,

	@Schema(description = "조회수", example = "42")
	Long views,

	@Schema(description = "작성자 실명", example = "홍길동")
	String realName,

	@Schema(description = "작성자 회원 ID", example = "5")
	Long memberId,

	@Schema(description = "캘린더 표시 여부 (true: 캘린더에 표시, false: 일반 게시글)", example = "true")
	Boolean showOnCalendar,

	@Schema(description = "캘린더에 표시될 날짜 (showOnCalendar가 true일 때만 유효)", example = "2024-01-15")
	LocalDate calendarDate,

	@Schema(description = "게시글 생성일시", example = "2024-01-10T14:30:00")
	LocalDateTime createdAt,

	@Schema(description = "게시글 수정일시", example = "2024-01-11T10:20:00")
	LocalDateTime updatedAt,

	@Schema(description = "첨부 파일 URL 목록", example = "['/files/posts/uuid1.jpg', '/files/posts/uuid2.pdf']")
	List<String> fileUrls
) {
	public static PostResponseDto fromDomain(Post post, String realName) {
		List<String> fileUrls = post.getFiles() != null
			? post.getFiles().stream()
				.map(file -> file.getFileUrl())
				.toList()
			: List.of();

		return new PostResponseDto(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getViews(),
			realName,
			post.getMemberId(),
			post.getShowOnCalendar(),
			post.getCalendarDate(),
			post.getCreatedAt(),
			post.getUpdatedAt(),
			fileUrls
		);
	}
}
