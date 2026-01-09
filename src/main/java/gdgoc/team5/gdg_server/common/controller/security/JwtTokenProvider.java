package gdgoc.team5.gdg_server.common.controller.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import gdgoc.team5.gdg_server.auth.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.accessExpiration}")
	private long accessTokenExpiration;
	@Value("${jwt.refreshExpiration}")
	private long refreshTokenExpiration;

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(Member member) {
		return generateToken(member, accessTokenExpiration);
	}

	public String generateRefreshToken(Member member) {
		return generateToken(member, refreshTokenExpiration);
	}

	private String generateToken(Member member, long expiration) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);

		return Jwts.builder()
			.subject(String.valueOf(member.getId()))
			.claim("username", member.getUsername())
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(getSecretKey())
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Long getMemberIdFromToken(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(getSecretKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return Long.parseLong(claims.getSubject());
	}
}
