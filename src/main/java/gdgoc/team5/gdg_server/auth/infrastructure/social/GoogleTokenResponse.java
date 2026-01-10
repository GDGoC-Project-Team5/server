package gdgoc.team5.gdg_server.auth.infrastructure.social;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenResponse(
	@JsonProperty("id_token")
	String idToken,

	@JsonProperty("access_token")
	String accessToken,

	@JsonProperty("expires_in")
	Integer expiresIn,

	@JsonProperty("token_type")
	String tokenType,

	@JsonProperty("refresh_token")
	String refreshToken,

	String scope
) {
}
