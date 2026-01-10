package gdgoc.team5.gdg_server.post.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gdgoc.team5.gdg_server.common.domain.BaseEntity;
import gdgoc.team5.gdg_server.file.domain.File;
import gdgoc.team5.gdg_server.post.controller.request.PostRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@Column(name = "post_id")
	private Long id;

	@Builder.Default
	@Column(nullable = false)
	private Long views = 0L;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Boolean showOnCalendar; // 캘린더 표시 여부

	private LocalDate calendarDate; // 캘린더 표시 날짜

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<File> files = new ArrayList<>();

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
