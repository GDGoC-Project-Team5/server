package gdgoc.team5.gdg_server.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdgoc.team5.gdg_server.auth.controller.request.RefreshTokenRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.SignUpRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.SocialLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.TestLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.response.LoginResponseDto;
import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.infrastructure.social.GoogleOidcService;
import gdgoc.team5.gdg_server.auth.infrastructure.social.GoogleTokenResponse;
import gdgoc.team5.gdg_server.auth.infrastructure.social.GoogleUserInfo;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.common.controller.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final GoogleOidcService googleOidcService;

	@Transactional
	public LoginResponseDto testLogin(TestLoginRequestDto dto, HttpServletResponse response) {
		Optional<Member> member = memberRepository.findByUsername(dto.username());
		Member newMember = member.orElseGet(() -> memberRepository.save(Member.testSignUp(dto)));

		setJwtTokensToHeader(newMember, response);
		return LoginResponseDto.fromDomain(newMember);
	}

	private void setJwtTokensToHeader(Member member, HttpServletResponse response) {
		String accessToken = jwtTokenProvider.generateAccessToken(member);
		String refreshToken = jwtTokenProvider.generateRefreshToken(member);

		response.setHeader("Authorization", "Bearer " + accessToken);
		response.setHeader("Refresh-Token", refreshToken);
	}

	@Transactional
	public LoginResponseDto signup(SignUpRequestDto dto, HttpServletResponse response) {
		Member member = new Member(dto);
		memberRepository.save(member);

		setJwtTokensToHeader(member, response);
		return LoginResponseDto.fromDomain(member);
	}

	@Transactional
	public LoginResponseDto socialLogin(SocialLoginRequestDto dto, String redirectUri, HttpServletResponse response) {
		// 1. 인가 코드로 Google 토큰 받기
		GoogleTokenResponse tokenResponse = googleOidcService.getTokens(dto.code(), redirectUri);

		// 2. ID Token 검증 및 사용자 정보 추출
		GoogleUserInfo userInfo = googleOidcService.verifyAndGetUserInfo(tokenResponse.idToken());

		// 3. socialId로 기존 회원 조회
		Optional<Member> existingMember = memberRepository.findBySocialId(userInfo.sub());

		// 4. 회원이 없으면 새로 가입, 있으면 기존 회원 사용
		Member member = existingMember.orElseGet(() ->
			memberRepository.save(Member.socialSignUp(
				userInfo.sub(),
				userInfo.email(),
				userInfo.name(),
				dto.memberType()
			))
		);

		// 5. JWT 토큰 발급
		setJwtTokensToHeader(member, response);

		return LoginResponseDto.fromDomain(member);
	}

	public void refreshAccessToken(RefreshTokenRequestDto dto, HttpServletResponse response) {
		String refreshToken = dto.refreshToken();

		// refresh token 유효성 검증
		if (!jwtTokenProvider.validateToken(refreshToken)) {
			throw new IllegalArgumentException("유효하지 않은 refresh token입니다.");
		}

		// refresh token에서 member ID 추출
		Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);

		// member 조회
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		// 새로운 access token 및 refresh token 생성
		String newAccessToken = jwtTokenProvider.generateAccessToken(member);
		String newRefreshToken = jwtTokenProvider.generateRefreshToken(member);

		// 응답 헤더에 새 토큰 설정
		response.setHeader("Authorization", "Bearer " + newAccessToken);
		response.setHeader("Refresh-Token", newRefreshToken);
	}
}
