package gdgoc.team5.gdg_server.post.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequestDto(
	@NotBlank(message = "제목은 필수입니다.")
	String title,

	@NotBlank(message = "내용은 필수입니다.")
	String content,

	@NotNull(message = "캘린더 표시 여부는 필수입니다.")
	Boolean showOnCalendar,

	LocalDate calendarDate
) {
	public PostRequestDto {
		// 캘린더 표시가 true인데 날짜가 없으면 예외
		if (Boolean.TRUE.equals(showOnCalendar) && calendarDate == null) {
			throw new IllegalArgumentException("캘린더 표시가 활성화된 경우 날짜는 필수입니다.");
		}
	}
}
