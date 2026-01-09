package gdgoc.team5.gdg_server.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.common.controller.security.JwtAuthenticationFilter;
import gdgoc.team5.gdg_server.common.controller.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			// CSRF 비활성화 (JWT 사용)
			.csrf(AbstractHttpConfigurer::disable)
			// 세션 비활성화 (Stateless)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 기본 로그인 방식 비활성화
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			// 요청 인가 규칙 설정
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/api/v1/auth/**",
					"/swagger-ui/**",
					"/v3/api-docs/**"
				).permitAll()
				.anyRequest().authenticated()
			)

			// 인증 실패 시 401 에러
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint((request, response, authException) ->
					response.sendError(401, "Unauthorized")
				)
			)

			// JWT 필터 추가
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtTokenProvider, memberRepository),
				UsernamePasswordAuthenticationFilter.class
			)
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}