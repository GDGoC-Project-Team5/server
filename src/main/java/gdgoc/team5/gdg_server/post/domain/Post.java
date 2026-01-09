package gdgoc.team5.gdg_server.post.domain;

import java.time.LocalDate;

import gdgoc.team5.gdg_server.common.domain.BaseEntity;
import gdgoc.team5.gdg_server.post.dto.PostRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Builder.Default
	@Column(nullable = false)
	private Long views = 0L;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	private String fileName;        // 원본 파일명

	private String filePath;        // 저장된 파일 경로

	private Long fileSize;          // 파일 크기

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Boolean showOnCalendar; // 캘린더 표시 여부

	private LocalDate calendarDate; // 캘린더 표시 날짜

	public static Post createPost(PostRequestDto dto, Long memberId) {
		return Post.builder()
			.title(dto.title())
			.content(dto.content())
			.memberId(memberId)
			.showOnCalendar(dto.showOnCalendar())
			.calendarDate(dto.calendarDate())
			.views(0L)
			.build();
	}
}
