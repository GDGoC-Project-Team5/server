package gdgoc.team5.gdg_server.post.controller.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "게시글 작성 요청")
public record PostRequestDto(
	@Schema(description = "게시글 제목 (필수)", example = "GDG 정기 모임 공지", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "제목은 필수입니다.")
	String title,

	@Schema(description = "게시글 본문 내용 (필수)", example = "다음 주 수요일 7시에 정기 모임이 있습니다.", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "내용은 필수입니다.")
	String content,

	@Schema(description = "캘린더 표시 여부 (필수, true일 경우 calendarDate도 필수)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "캘린더 표시 여부는 필수입니다.")
	Boolean showOnCalendar,

	@Schema(description = "캘린더에 표시될 날짜 (showOnCalendar가 true일 때 필수)", example = "2024-01-15")
	LocalDate calendarDate
) {
	public PostRequestDto {
		// 캘린더 표시가 true인데 날짜가 없으면 예외
		if (Boolean.TRUE.equals(showOnCalendar) && calendarDate == null) {
			throw new IllegalArgumentException("캘린더 표시가 활성화된 경우 날짜는 필수입니다.");
		}
	}
}
