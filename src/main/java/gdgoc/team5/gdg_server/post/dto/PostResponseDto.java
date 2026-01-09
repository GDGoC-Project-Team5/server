package gdgoc.team5.gdg_server.post.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import gdgoc.team5.gdg_server.post.domain.Post;

public record PostResponseDto(
	Long id,
	Long views,
	String title,
	String content,
	Long memberId,
	String fileName,
	String filePath,
	Long fileSize,
	Boolean showOnCalendar,
	LocalDate calendarDate,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public PostResponseDto(Post post) {
		this(
			post.getId(),
			post.getViews(),
			post.getTitle(),
			post.getContent(),
			post.getMemberId(),
			post.getFileName(),
			post.getFilePath(),
			post.getFileSize(),
			post.getShowOnCalendar(),
			post.getCalendarDate(),
			post.getCreatedAt(),
			post.getUpdatedAt()
		);
	}
}
