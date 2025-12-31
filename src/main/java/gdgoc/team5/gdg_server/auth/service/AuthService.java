package gdgoc.team5.gdg_server.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdgoc.team5.gdg_server.auth.controller.request.SignUpRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.SocialLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.TestLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.response.LoginResponseDto;
import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.jwt.JwtTokenProvider;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

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

	public LoginResponseDto socialLogin(SocialLoginRequestDto dto, HttpServletResponse response) {
		return null;
	}
}
