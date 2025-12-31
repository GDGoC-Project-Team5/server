package gdgoc.team5.gdg_server.auth.controller.request;

import gdgoc.team5.gdg_server.auth.domain.MemberType;
import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequestDto(
	@NotBlank(message = "code는 빈칸일 수 없습니다.")
	String code,

	@NotBlank(message = "타입은 빈칸일 수 없습니다. (ex. GOOGLE 등)")
	MemberType memberType
) {
}
