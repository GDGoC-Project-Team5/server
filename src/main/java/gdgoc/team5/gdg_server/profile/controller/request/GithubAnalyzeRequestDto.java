package gdgoc.team5.gdg_server.profile.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "GitHub 분석 요청")
public record GithubAnalyzeRequestDto(
	@Schema(description = "GitHub 사용자 ID", example = "ChooJG")
	@NotBlank(message = "GitHub ID는 필수입니다.")
	String githubId
) {
}
