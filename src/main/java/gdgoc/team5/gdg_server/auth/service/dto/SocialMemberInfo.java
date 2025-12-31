package gdgoc.team5.gdg_server.auth.service.dto;

public record SocialMemberInfo(
	String email,
	String tempUsername,
	String socialId
) {
}
