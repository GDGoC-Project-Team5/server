package gdgoc.team5.gdg_server.post.controller.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import gdgoc.team5.gdg_server.post.domain.Post;

public record PostResponseDto(
	Long postId,
	String title,
	String content,
	Long views,
	String realName,
	Long memberId,
	Boolean showOnCalendar,
	LocalDate calendarDate,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static PostResponseDto fromDomain(Post post, String realName) {
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
			post.getUpdatedAt()
		);
	}
}
