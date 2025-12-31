package gdgoc.team5.gdg_server.auth.controller.request;

import jakarta.validation.constraints.NotBlank;

public record TestLoginRequestDto(
	@NotBlank(message = "username은 빈칸일 수 없습니다.")
	String username,

	@NotBlank(message = "승인 여부는 빈칸일 수 없습니다.")
	Boolean isPending,

	@NotBlank(message = "관리자 계정 여부는 빈칸일 수 없습니다.")
	Boolean isAdmin
) {
}
