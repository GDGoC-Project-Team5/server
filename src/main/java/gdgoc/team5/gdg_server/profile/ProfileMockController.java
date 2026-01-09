package gdgoc.team5.gdg_server.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.common.controller.argresolver.AuthenticatedToken;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mock/profile")
@Tag(name = "Profile Mock", description = "프로필 관련 Mock API")
public class ProfileMockController {

	// DTO 클래스들
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProfileSummaryDto {
		@Schema(description = "멤버 ID", example = "1")
		private Long id;

		@Schema(description = "이름", example = "홍길동")
		private String name;

		@Schema(description = "파트", example = "BE")
		private String part;

		@Schema(description = "기수", example = "5")
		private Integer generation;

		@Schema(description = "기술스택", example = "[\"Java\", \"Spring Boot\", \"MySQL\"]")
		private List<String> techStack;

		@Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
		private String profileImageUrl;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProfileDetailDto {
		@Schema(description = "멤버 ID", example = "1")
		private Long id;

		@Schema(description = "이름", example = "홍길동")
		private String name;

		@Schema(description = "파트", example = "BE")
		private String part;

		@Schema(description = "기수", example = "5")
		private Integer generation;

		@Schema(description = "한줄소개", example = "백엔드 개발을 좋아하는 개발자입니다.")
		private String introduction;

		@Schema(description = "기술스택", example = "[\"Java\", \"Spring Boot\", \"MySQL\"]")
		private List<String> techStack;

		@Schema(description = "포트폴리오 URL", example = "https://portfolio.example.com")
		private String portfolioUrl;

		@Schema(description = "깃허브 URL", example = "https://github.com/username")
		private String githubUrl;

		@Schema(description = "링크드인 URL", example = "https://linkedin.com/in/username")
		private String linkedinUrl;

		@Schema(description = "이메일", example = "user@example.com")
		private String email;

		@Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
		private String profileImageUrl;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProfileUpdateRequestDto {
		@Schema(description = "한줄소개", example = "백엔드 개발을 좋아하는 개발자입니다.")
		private String introduction;

		@Schema(description = "기술스택", example = "[\"Java\", \"Spring Boot\", \"MySQL\"]")
		private List<String> techStack;

		@Schema(description = "포트폴리오 URL", example = "https://portfolio.example.com")
		private String portfolioUrl;

		@Schema(description = "깃허브 URL", example = "https://github.com/username")
		private String githubUrl;

		@Schema(description = "링크드인 URL", example = "https://linkedin.com/in/username")
		private String linkedinUrl;

		@Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
		private String profileImageUrl;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PageResponse<T> {
		@Schema(description = "현재 페이지 내용")
		private List<T> content;

		@Schema(description = "현재 페이지 번호", example = "0")
		private int pageNumber;

		@Schema(description = "페이지 크기", example = "10")
		private int pageSize;

		@Schema(description = "전체 요소 개수", example = "50")
		private long totalElements;

		@Schema(description = "전체 페이지 개수", example = "5")
		private int totalPages;

		@Schema(description = "마지막 페이지 여부", example = "false")
		private boolean last;
	}

	// Mock 데이터 생성
	private List<ProfileDetailDto> createMockProfiles() {
		return Arrays.asList(
			ProfileDetailDto.builder()
				.id(1L)
				.name("홍길동")
				.part("BE")
				.generation(5)
				.introduction("백엔드 개발을 좋아하는 개발자입니다.")
				.techStack(Arrays.asList("Java", "Spring Boot", "MySQL"))
				.portfolioUrl("https://portfolio.example.com/hong")
				.githubUrl("https://github.com/hong")
				.linkedinUrl("https://linkedin.com/in/hong")
				.email("hong@example.com")
				.profileImageUrl("https://example.com/profile1.jpg")
				.build(),
			ProfileDetailDto.builder()
				.id(2L)
				.name("김철수")
				.part("FE")
				.generation(5)
				.introduction("사용자 경험을 중시하는 프론트엔드 개발자입니다.")
				.techStack(Arrays.asList("React", "TypeScript", "Next.js"))
				.portfolioUrl("https://portfolio.example.com/kim")
				.githubUrl("https://github.com/kim")
				.linkedinUrl("https://linkedin.com/in/kim")
				.email("kim@example.com")
				.profileImageUrl("https://example.com/profile2.jpg")
				.build(),
			ProfileDetailDto.builder()
				.id(3L)
				.name("이영희")
				.part("DESIGN")
				.generation(4)
				.introduction("감각적인 디자인을 추구하는 디자이너입니다.")
				.techStack(Arrays.asList("Figma", "Adobe XD", "Photoshop"))
				.portfolioUrl("https://portfolio.example.com/lee")
				.githubUrl("https://github.com/lee")
				.linkedinUrl("https://linkedin.com/in/lee")
				.email("lee@example.com")
				.profileImageUrl("https://example.com/profile3.jpg")
				.build(),
			ProfileDetailDto.builder()
				.id(4L)
				.name("박민수")
				.part("APP")
				.generation(5)
				.introduction("모바일 앱 개발에 열정을 가진 개발자입니다.")
				.techStack(Arrays.asList("Flutter", "Dart", "Firebase"))
				.portfolioUrl("https://portfolio.example.com/park")
				.githubUrl("https://github.com/park")
				.linkedinUrl("https://linkedin.com/in/park")
				.email("park@example.com")
				.profileImageUrl("https://example.com/profile4.jpg")
				.build(),
			ProfileDetailDto.builder()
				.id(5L)
				.name("최지은")
				.part("AI")
				.generation(5)
				.introduction("인공지능과 머신러닝에 관심이 많은 개발자입니다.")
				.techStack(Arrays.asList("Python", "TensorFlow", "PyTorch"))
				.portfolioUrl("https://portfolio.example.com/choi")
				.githubUrl("https://github.com/choi")
				.linkedinUrl("https://linkedin.com/in/choi")
				.email("choi@example.com")
				.profileImageUrl("https://example.com/profile5.jpg")
				.build(),
			ProfileDetailDto.builder()
				.id(6L)
				.name("정수진")
				.part("BE")
				.generation(4)
				.introduction("확장 가능한 시스템 설계에 관심이 많습니다.")
				.techStack(Arrays.asList("Kotlin", "Spring", "PostgreSQL"))
				.portfolioUrl("https://portfolio.example.com/jung")
				.githubUrl("https://github.com/jung")
				.linkedinUrl("https://linkedin.com/in/jung")
				.email("jung@example.com")
				.profileImageUrl("https://example.com/profile6.jpg")
				.build()
		);
	}

	// 1. 전체 유저 데이터 불러오기 (검색 필터, 페이지네이션 적용)
	@GetMapping
	@Operation(summary = "전체 프로필 조회", description = "검색 필터와 페이지네이션을 적용하여 전체 멤버 프로필을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = PageResponse.class)))
	})
	public ResponseEntity<PageResponse<ProfileSummaryDto>> getAllProfiles(
		@Parameter(description = "이름 검색 키워드") @RequestParam(required = false) String name,
		@Parameter(description = "파트 필터 (FE, BE, DESIGN, APP, AI)") @RequestParam(required = false) String part,
		@Parameter(description = "기수 필터 (1-5)") @RequestParam(required = false) Integer generation,
		@Parameter(description = "기술스택 검색 키워드") @RequestParam(required = false) String techStack,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
		@AuthenticatedToken TokenInfo tokenInfo
	) {

		log.info("token: {}", tokenInfo.memberId());
		List<ProfileDetailDto> allProfiles = createMockProfiles();

		// 필터 적용
		List<ProfileDetailDto> filteredProfiles = allProfiles.stream()
			.filter(p -> name == null || p.getName().contains(name))
			.filter(p -> part == null || p.getPart().equals(part))
			.filter(p -> generation == null || p.getGeneration().equals(generation))
			.filter(p -> techStack == null || p.getTechStack().stream()
				.anyMatch(tech -> tech.toLowerCase().contains(techStack.toLowerCase())))
			.collect(Collectors.toList());

		// 페이지네이션 적용
		int start = page * size;
		int end = Math.min(start + size, filteredProfiles.size());
		List<ProfileDetailDto> pagedProfiles = start > filteredProfiles.size()
			? new ArrayList<>()
			: filteredProfiles.subList(start, end);

		// Summary DTO로 변환
		List<ProfileSummaryDto> summaries = pagedProfiles.stream()
			.map(p -> ProfileSummaryDto.builder()
				.id(p.getId())
				.name(p.getName())
				.part(p.getPart())
				.generation(p.getGeneration())
				.techStack(p.getTechStack())
				.profileImageUrl(p.getProfileImageUrl())
				.build())
			.collect(Collectors.toList());

		// PageResponse 생성
		PageResponse<ProfileSummaryDto> response = PageResponse.<ProfileSummaryDto>builder()
			.content(summaries)
			.pageNumber(page)
			.pageSize(size)
			.totalElements(filteredProfiles.size())
			.totalPages((int)Math.ceil((double)filteredProfiles.size() / size))
			.last(end >= filteredProfiles.size())
			.build();

		return ResponseEntity.ok(response);
	}

	// 2. 단일 멤버 정보 조회
	@GetMapping("/{id}")
	@Operation(summary = "단일 프로필 조회", description = "특정 멤버의 상세 프로필을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = ProfileDetailDto.class))),
		@ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
	})
	public ResponseEntity<ProfileDetailDto> getProfileById(
		@Parameter(description = "멤버 ID", example = "1") @PathVariable Long id
	) {
		List<ProfileDetailDto> profiles = createMockProfiles();

		return profiles.stream()
			.filter(p -> p.getId().equals(id))
			.findFirst()
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	// 3. 프로필 수정
	@PutMapping("/{id}")
	@Operation(summary = "프로필 수정", description = "특정 멤버의 프로필 정보를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "수정 성공",
			content = @Content(schema = @Schema(implementation = ProfileDetailDto.class))),
		@ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
	})
	public ResponseEntity<ProfileDetailDto> updateProfile(
		@Parameter(description = "멤버 ID", example = "1") @PathVariable Long id,
		@RequestBody ProfileUpdateRequestDto request
	) {
		List<ProfileDetailDto> profiles = createMockProfiles();

		return profiles.stream()
			.filter(p -> p.getId().equals(id))
			.findFirst()
			.map(profile -> {
				// 기존 프로필에 업데이트 내용 반영
				ProfileDetailDto updated = ProfileDetailDto.builder()
					.id(profile.getId())
					.name(profile.getName())
					.part(profile.getPart())
					.generation(profile.getGeneration())
					.introduction(request.getIntroduction())
					.techStack(request.getTechStack())
					.portfolioUrl(request.getPortfolioUrl())
					.githubUrl(request.getGithubUrl())
					.linkedinUrl(request.getLinkedinUrl())
					.email(profile.getEmail())
					.profileImageUrl(request.getProfileImageUrl())
					.build();

				return ResponseEntity.ok(updated);
			})
			.orElse(ResponseEntity.notFound().build());
	}
}
