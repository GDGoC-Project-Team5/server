package gdgoc.team5.gdg_server.auth.controller.response;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.domain.MemberRole;
import gdgoc.team5.gdg_server.auth.domain.MemberType;

public record LoginResponseDto(
	String realName,
	MemberRole memberRole,
	MemberType memberType,
	String email,
	Boolean isAdmin,
	Boolean isPending
) {
	public static LoginResponseDto fromDomain(Member member) {
		return new LoginResponseDto(
			member.getRealName(),
			member.getMemberRole(),
			member.getMemberType(),
			member.getEmail(),
			member.getIsAdmin(),
			member.getIsPending()
		);
	}
}
