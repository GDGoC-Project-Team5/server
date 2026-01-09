package gdgoc.team5.gdg_server.calendar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.calendar.controller.response.CalendarResponseDto;
import gdgoc.team5.gdg_server.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "캘린더", description = "캘린더 일정 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calendar")
public class CalendarController {

	private final CalendarService calendarService;

	@Operation(
		summary = "캘린더 일정 조회",
		description = "특정 년/월의 캘린더 일정을 조회합니다. showOnCalendar가 true인 게시글만 반환됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "캘린더 일정 조회 성공",
			content = @Content(
				mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = CalendarResponseDto.class))
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (년/월 파라미터 오류)")
	})
	@GetMapping("")
	public ResponseEntity<List<CalendarResponseDto>> getCalendarEvents(
		@Parameter(description = "년 (예: 2024)", required = true, example = "2024")
		@RequestParam int year,
		@Parameter(description = "월 (1-12)", required = true, example = "1")
		@RequestParam int month
	) {
		// 월 범위 체크
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("월은 1에서 12 사이의 값이어야 합니다.");
		}

		List<CalendarResponseDto> events = calendarService.getCalendarEvents(year, month);
		return ResponseEntity.ok(events);
	}
}
