package gdgoc.team5.gdg_server.auth.controller.request;

import gdgoc.team5.gdg_server.auth.domain.MemberRole;
import gdgoc.team5.gdg_server.auth.domain.MemberType;

public record SignUpRequestDto(
	String username,
	String email,
	String realName,
	MemberRole memberRole,
	MemberType memberType,
	Integer generation,
	String password
) {
}
