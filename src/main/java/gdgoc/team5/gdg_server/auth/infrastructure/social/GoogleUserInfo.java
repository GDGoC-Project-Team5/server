package gdgoc.team5.gdg_server.auth.infrastructure.social;

public record GoogleUserInfo(
	String sub,              // Google user ID
	String email,
	Boolean emailVerified,
	String name,
	String picture,
	String givenName,
	String familyName
) {
}
