package gdgoc.team5.gdg_server.auth.controller.request;

import gdgoc.team5.gdg_server.auth.domain.MemberType;
import gdgoc.team5.gdg_server.profile.domain.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(
	@NotBlank(message = "아이디는 빈칸일 수 없습니다.")
	String username,

	@NotBlank(message = "이메일은 빈칸일 수 없습니다.")
	@Email(message = "이메일 형식이어야 합니다.")
	String email,

	@NotBlank(message = "실명은 빈칸일 수 없습니다.")
	String realName,

	@NotBlank(message = "멤버 구분은 빈칸일 수 없습니다.")
	MemberRole memberRole,

	@NotBlank(message = "멤버 타입은 빈칸일 수 없습니다.")
	MemberType memberType
) {
}
