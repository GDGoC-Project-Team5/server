package gdgoc.team5.gdg_server.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestLoginRequestDto(
	@NotBlank(message = "username은 빈칸일 수 없습니다.")
	String username,

	@NotNull(message = "승인 여부는 빈칸일 수 없습니다.")
	Boolean isPending,

	@NotNull(message = "관리자 계정 여부는 빈칸일 수 없습니다.")
	Boolean isAdmin
) {
}
