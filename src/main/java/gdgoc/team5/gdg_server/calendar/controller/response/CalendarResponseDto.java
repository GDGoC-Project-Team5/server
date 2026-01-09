package gdgoc.team5.gdg_server.calendar.controller.response;

import java.time.LocalDate;

import gdgoc.team5.gdg_server.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "캘린더 일정 조회 응답")
public record CalendarResponseDto(
	@Schema(description = "게시글 ID", example = "1")
	Long postId,

	@Schema(description = "게시글 제목", example = "GDG 정기 모임")
	String title,

	@Schema(description = "캘린더 표시 날짜", example = "2024-01-15")
	LocalDate calendarDate,

	@Schema(description = "작성자 실명", example = "홍길동")
	String authorName
) {
	public static CalendarResponseDto fromDomain(Post post, String authorName) {
		return new CalendarResponseDto(
			post.getId(),
			post.getTitle(),
			post.getCalendarDate(),
			authorName
		);
	}
}
