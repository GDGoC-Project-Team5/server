package gdgoc.team5.gdg_server.auth.controller.request;

import gdgoc.team5.gdg_server.auth.domain.MemberType;
import gdgoc.team5.gdg_server.profile.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원가입 요청")
public record SignUpRequestDto(
	@Schema(description = "사용자 아이디", example = "newuser123")
	@NotBlank(message = "아이디는 빈칸일 수 없습니다.")
	String username,

	@Schema(description = "이메일 주소", example = "user@example.com")
	@NotBlank(message = "이메일은 빈칸일 수 없습니다.")
	@Email(message = "이메일 형식이어야 합니다.")
	String email,

	@Schema(description = "실명", example = "홍길동")
	@NotBlank(message = "실명은 빈칸일 수 없습니다.")
	String realName,

	@Schema(description = "멤버 역할 (MEMBER: 멤버, CORE: 코어, LEAD: 리드)", example = "MEMBER")
	@NotBlank(message = "멤버 구분은 빈칸일 수 없습니다.")
	MemberRole memberRole,

	@Schema(description = "로그인 타입 (GOOGLE: 구글, LOCAL: 로컬 등)", example = "LOCAL")
	@NotBlank(message = "멤버 타입은 빈칸일 수 없습니다.")
	MemberType memberType
) {
}
