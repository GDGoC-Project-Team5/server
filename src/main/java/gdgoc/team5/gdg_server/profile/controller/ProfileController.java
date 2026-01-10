package gdgoc.team5.gdg_server.profile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gdgoc.team5.gdg_server.common.controller.argresolver.AuthenticatedToken;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import gdgoc.team5.gdg_server.profile.controller.request.GithubAnalyzeRequestDto;
import gdgoc.team5.gdg_server.profile.controller.request.ProfileRequestDto;
import gdgoc.team5.gdg_server.profile.controller.response.GithubAnalyzeResponseDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileDetailResponseDto;
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

	@Operation(
		summary = "특정 멤버 프로필 상세 조회",
		description = "특정 멤버의 프로필 상세 정보를 조회합니다. 이름, 이메일, SNS 링크, 한줄소개, 학과, 기술스택을 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "멤버 프로필 조회 성공",
			content = @Content(schema = @Schema(implementation = ProfileDetailResponseDto.class))
		),
		@ApiResponse(responseCode = "404", description = "회원 또는 프로필을 찾을 수 없음")
	})
	@GetMapping("/members/{memberId}")
	public ResponseEntity<ProfileDetailResponseDto> getMemberProfile(
		@Parameter(description = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId
	) {
		ProfileDetailResponseDto profile = profileService.getMemberProfile(memberId);
		return ResponseEntity.ok(profile);
	}

	@Operation(
		summary = "GitHub 프로필 분석",
		description = "GitHub 사용자 ID를 입력받아 외부 AI 분석 API(localhost:8000)를 호출하여 개발자 프로필을 분석합니다. " +
			"프로필 정보, 사용 언어 통계, 협업 능력, AI 분석 결과를 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "GitHub 분석 성공",
			content = @Content(schema = @Schema(implementation = GithubAnalyzeResponseDto.class))
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (GitHub ID가 유효하지 않거나 분석 서버 요청 오류)"),
		@ApiResponse(responseCode = "500", description = "분석 서버 오류 또는 네트워크 오류")
	})
	@PostMapping("/github-analyze")
	public ResponseEntity<GithubAnalyzeResponseDto> analyzeGithub(
		@Parameter(description = "GitHub 분석 요청 (GitHub 사용자 ID)", required = true)
		@RequestBody GithubAnalyzeRequestDto request
	) {
		GithubAnalyzeResponseDto response = profileService.analyzeGithub(request.githubId());
		return ResponseEntity.ok(response);
	}

	@Operation(
		summary = "프로필 이미지 업로드",
		description = "로그인한 사용자의 프로필 이미지를 업로드합니다. " +
			"이미지 파일만 업로드 가능하며, 최대 크기는 2MB입니다. " +
			"기존 프로필 이미지가 있다면 삭제되고 새 이미지로 교체됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "프로필 이미지 업로드 성공. 업로드된 이미지의 URL을 반환합니다."
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 형식 오류 또는 크기 초과)"),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@PostMapping(value = "/image", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadProfileImage(
		@Parameter(description = "업로드할 프로필 이미지 (최대 2MB)", required = true)
		@RequestPart("image") MultipartFile image,
		@AuthenticatedToken TokenInfo tokenInfo
	) {
		String imageUrl = profileService.updateProfileImage(tokenInfo.memberId(), image);
		return ResponseEntity.ok(imageUrl);
	}

}
