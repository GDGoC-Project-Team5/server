package gdgoc.team5.gdg_server.auth.infrastructure.social;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOidcService {

	@Value("${google.oauth2.client-id}")
	private String clientId;

	@Value("${google.oauth2.client-secret}")
	private String clientSecret;

	@Value("${google.oauth2.redirect-uri-local}")
	private String redirectUriLocal;

	@Value("${google.oauth2.redirect-uri-prod}")
	private String redirectUriProd;

	@Value("${google.oauth2.token-uri}")
	private String tokenUri;

	private final RestTemplate restTemplate = new RestTemplate();

	/**
	 * 인가 코드를 사용하여 Google로부터 ID Token과 Access Token을 받아옵니다.
	 */
	public GoogleTokenResponse getTokens(String code, String redirectUri) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("redirect_uri", redirectUri);
		params.add("grant_type", "authorization_code");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(
			tokenUri,
			HttpMethod.POST,
			request,
			GoogleTokenResponse.class
		);

		if (response.getBody() == null) {
			throw new IllegalStateException("Google 토큰 응답이 비어있습니다.");
		}

		return response.getBody();
	}

	public String getRedirectUriLocal() {
		return redirectUriLocal;
	}

	public String getRedirectUriProd() {
		return redirectUriProd;
	}

	/**
	 * ID Token을 검증하고 사용자 정보를 추출합니다.
	 */
	public GoogleUserInfo verifyAndGetUserInfo(String idToken) {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				new NetHttpTransport(),
				new GsonFactory()
			)
				.setAudience(Collections.singletonList(clientId))
				.build();

			GoogleIdToken googleIdToken = verifier.verify(idToken);

			if (googleIdToken == null) {
				throw new IllegalArgumentException("유효하지 않은 ID Token입니다.");
			}

			GoogleIdToken.Payload payload = googleIdToken.getPayload();

			return new GoogleUserInfo(
				payload.getSubject(),
				payload.getEmail(),
				payload.getEmailVerified(),
				(String)payload.get("name"),
				(String)payload.get("picture"),
				(String)payload.get("given_name"),
				(String)payload.get("family_name")
			);

		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException("ID Token 검증 중 오류가 발생했습니다.", e);
		}
	}
}
