package gdgoc.team5.gdg_server.auth.controller.request;

import gdgoc.team5.gdg_server.auth.domain.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "소셜 로그인 요청")
public record SocialLoginRequestDto(
	@Schema(description = "OAuth 인증 코드 (소셜 서비스에서 발급받은 코드)", example = "4/0AY0e-g7...")
	@NotBlank(message = "code는 빈칸일 수 없습니다.")
	String code,

	@Schema(description = "소셜 로그인 타입 (GOOGLE: 구글 등)", example = "GOOGLE")
	@NotBlank(message = "타입은 빈칸일 수 없습니다. (ex. GOOGLE 등)")
	MemberType memberType
) {
}
