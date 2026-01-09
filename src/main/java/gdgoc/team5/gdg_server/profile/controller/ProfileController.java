package gdgoc.team5.gdg_server.profile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.common.controller.argresolver.AuthenticatedToken;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import gdgoc.team5.gdg_server.profile.controller.request.ProfileRequestDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileListResponseDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileResponseDto;
import gdgoc.team5.gdg_server.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "프로필", description = "프로필 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

	private final ProfileService profileService;

	@Operation(
		summary = "프로필 수정/생성",
		description = "로그인한 사용자의 프로필을 수정합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "프로필 수정/생성 성공",
			content = @Content(schema = @Schema(implementation = ProfileResponseDto.class))
		),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@PutMapping("")
	public ResponseEntity<ProfileResponseDto> upsertProfile(
		@RequestBody ProfileRequestDto requestDto,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		ProfileResponseDto response = profileService.upsertProfile(tokenInfo.memberId(), requestDto);
		return ResponseEntity.ok(response);
	}

	@Operation(
		summary = "내 프로필 조회",
		description = "로그인한 사용자의 프로필을 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "프로필 조회 성공",
			content = @Content(schema = @Schema(implementation = ProfileResponseDto.class))
		),
		@ApiResponse(responseCode = "404", description = "프로필을 찾을 수 없음"),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@GetMapping("")
	public ResponseEntity<ProfileResponseDto> getMyProfile(
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		ProfileResponseDto response = profileService.getProfile(tokenInfo.memberId());
		return ResponseEntity.ok(response);
	}

	@Operation(
		summary = "멤버 목록 조회",
		description = "기수별 멤버 목록을 조회합니다. generation 파라미터가 없으면 전체 멤버를 기수 오름차순으로 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "멤버 목록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = ProfileListResponseDto.class))
			)
		)
	})
	@GetMapping("/members")
	public ResponseEntity<List<ProfileListResponseDto>> getMembersByGeneration(
		@Parameter(required = true, description = "기수 (필수)", example = "5")
		@RequestParam(required = false) Integer generation
	) {
		List<ProfileListResponseDto> members = profileService.getMembersByGeneration(generation);
		return ResponseEntity.ok(members);
	}
}
