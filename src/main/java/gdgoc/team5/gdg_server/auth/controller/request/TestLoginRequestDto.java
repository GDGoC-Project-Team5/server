package gdgoc.team5.gdg_server.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "테스트 로그인 요청")
public record TestLoginRequestDto(
	@Schema(description = "사용자 아이디 (존재하지 않으면 자동 회원가입)", example = "testuser123")
	@NotBlank(message = "username은 빈칸일 수 없습니다.")
	String username,

	@Schema(description = "승인 대기 여부 (false: 승인됨, true: 승인 대기)", example = "false")
	@NotNull(message = "승인 여부는 빈칸일 수 없습니다.")
	Boolean isPending,

	@Schema(description = "관리자 계정 여부 (true: 관리자, false: 일반 사용자)", example = "false")
	@NotNull(message = "관리자 계정 여부는 빈칸일 수 없습니다.")
	Boolean isAdmin
) {
}
