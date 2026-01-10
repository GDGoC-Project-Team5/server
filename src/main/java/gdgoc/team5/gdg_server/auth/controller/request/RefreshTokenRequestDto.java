package gdgoc.team5.gdg_server.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "토큰 갱신 요청")
public record RefreshTokenRequestDto(
	@Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	@NotBlank(message = "refreshToken은 빈칸일 수 없습니다.")
	String refreshToken
) {
}
