package gdgoc.team5.gdg_server.auth.controller.response;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.domain.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponseDto(
	@Schema(description = "사용자 실명", example = "홍길동")
	String realName,

	@Schema(description = "로그인 타입 (GOOGLE: 구글, LOCAL: 로컬 등)", example = "LOCAL")
	MemberType memberType,

	@Schema(description = "이메일 주소", example = "user@example.com")
	String email,

	@Schema(description = "관리자 여부 (true: 관리자, false: 일반 사용자)", example = "false")
	Boolean isAdmin,

	@Schema(description = "승인 대기 여부 (true: 승인 대기, false: 승인됨)", example = "false")
	Boolean isPending
) {
	public static LoginResponseDto fromDomain(Member member) {
		return new LoginResponseDto(
			member.getRealName(),
			member.getMemberType(),
			member.getEmail(),
			member.getIsAdmin(),
			member.getIsPending()
		);
	}
}
