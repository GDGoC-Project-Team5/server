package gdgoc.team5.gdg_server.skill.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.skill.controller.response.SkillResponseDto;
import gdgoc.team5.gdg_server.skill.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "기술 스택", description = "기술 스택 검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/skills")
public class SkillController {

	private final SkillService skillService;

	@Operation(
		summary = "기술 스택 검색",
		description = "기술 스택을 검색합니다. 키워드가 없으면 전체 목록을 반환합니다. " +
			"클라이언트는 이 API로 기술 스택을 검색하고, 사용자가 선택한 기술들을 쉼표로 구분하여 프로필에 저장하면 됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "기술 스택 검색 성공",
			content = @Content(
				mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = SkillResponseDto.class))
			)
		)
	})
	@GetMapping("")
	public ResponseEntity<List<SkillResponseDto>> searchSkills(
		@Parameter(description = "검색 키워드 (선택사항, 없으면 전체 조회)", example = "Java")
		@RequestParam(required = false) String search
	) {
		List<SkillResponseDto> skills = skillService.searchSkills(search);
		return ResponseEntity.ok(skills);
	}
}
