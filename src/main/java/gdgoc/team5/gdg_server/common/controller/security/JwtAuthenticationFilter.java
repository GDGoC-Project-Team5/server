package gdgoc.team5.gdg_server.common.controller.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		// 1. Authorization 헤더에서 JWT 추출
		String token = extractTokenFromRequest(request);

		// 2. JWT 검증 및 인증 처리
		if (token != null && jwtTokenProvider.validateToken(token)) {
			Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

			// 3. Member 조회
			Member member = memberRepository.findById(memberId)
				.orElse(null);

			if (member != null) {
				// 4. TokenInfo를 Request Attribute에 설정 (ArgumentResolver에서 사용)
				TokenInfo tokenInfo = TokenInfo.from(
					member.getId(),
					"Bearer",
					member.getIsAdmin()
				);
				request.setAttribute("tokenInfo", tokenInfo);

				// 5. SecurityContext(ThreadLocal)에 인증 정보 저장
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(
						member,
						null
					);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		// 6. 다음 필터로 진행
		filterChain.doFilter(request, response);
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
