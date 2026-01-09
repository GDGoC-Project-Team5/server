package gdgoc.team5.gdg_server.profile.controller.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 분석 응답")
public record GithubAnalyzeResponseDto(
	@Schema(description = "프로필 정보")
	Profile profile,

	@Schema(description = "사용 언어 통계")
	List<Language> languages,

	@Schema(description = "협업 통계")
	TeamworkStats teamworkStats,

	@Schema(description = "분석 결과")
	Analysis analysis
) {
	@Schema(description = "GitHub 프로필 정보")
	public record Profile(
		@Schema(description = "이름", example = "홍길동")
		String name,

		@Schema(description = "GitHub ID", example = "ChooJG")
		String githubId,

		@Schema(description = "아바타 URL", example = "https://avatars.githubusercontent.com/u/12345")
		String avatarUrl,

		@Schema(description = "자기소개", example = "Backend Developer")
		String bio,

		@Schema(description = "프로필 URL", example = "https://github.com/ChooJG")
		String profileUrl,

		@Schema(description = "마지막 업데이트 시간", example = "2024-01-10T12:00:00Z")
		String updatedAt
	) {
	}

	@Schema(description = "언어 사용 통계")
	public record Language(
		@Schema(description = "언어 이름", example = "Java")
		String name,

		@Schema(description = "사용 비율 (%)", example = "45")
		@JsonProperty("percent")
		Integer percent
	) {
	}

	@Schema(description = "협업 통계")
	public record TeamworkStats(
		@Schema(description = "전체 레포지토리 수", example = "25")
		Integer totalRepos,

		@Schema(description = "협업 레포지토리 수", example = "15")
		Integer collaborativeRepos,

		@Schema(description = "개인 레포지토리 수", example = "10")
		Integer soloRepos
	) {
	}

	@Schema(description = "AI 분석 결과")
	public record Analysis(
		@Schema(description = "강점", example = "[\"Java 백엔드 개발\", \"협업 능력\"]")
		List<String> strengths,

		@Schema(description = "협업 스타일", example = "[\"적극적인 코드 리뷰\", \"명확한 커뮤니케이션\"]")
		List<String> teamwork,

		@Schema(description = "성장 포인트", example = "[\"프론트엔드 스킬 향상\", \"테스트 커버리지 증가\"]")
		List<String> growthFocus
	) {
	}
}
