package gdgoc.team5.gdg_server.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.auth.controller.request.RefreshTokenRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.SignUpRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.SocialLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.TestLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.response.LoginResponseDto;
import gdgoc.team5.gdg_server.auth.infrastructure.social.GoogleOidcService;
import gdgoc.team5.gdg_server.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "회원가입, 로그인, 소셜 로그인 등 인증 관련 API")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final GoogleOidcService googleOidcService;

	@Operation(
		summary = "테스트 로그인",
		description = "개발 환경에서 사용하는 간편 로그인 API입니다. " +
			"존재하지 않는 username을 입력하면 자동으로 회원가입이 진행되고, " +
			"존재하는 username을 입력하면 로그인이 진행됩니다. "
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 또는 회원가입 성공. JWT 토큰이 쿠키에 설정됩니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = LoginResponseDto.class)
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 필드 누락 또는 형식 오류)")
	})
	@PostMapping("/login/test")
	public ResponseEntity<LoginResponseDto> testLogin(
		@Parameter(description = "테스트 로그인 요청 정보", required = true)
		@RequestBody @Valid TestLoginRequestDto dto,
		HttpServletResponse response
	) {
		LoginResponseDto loginResponseDto = authService.testLogin(dto, response);
		return ResponseEntity.ok(loginResponseDto);
	}

	@Operation(
		summary = "회원가입",
		description = "신규 회원가입 API입니다. " +
			"memberType은 로그인 형식(구글, 로컬 등)을 구분합니다. " +
			"회원가입 완료 후 JWT 토큰이 쿠키에 자동으로 설정됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "회원가입 성공. JWT 토큰이 쿠키에 설정됩니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = LoginResponseDto.class)
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 필드 누락, 이메일 형식 오류, 중복 회원 등)")
	})
	@PostMapping("/signup")
	public ResponseEntity<LoginResponseDto> signup(
		@Parameter(description = "회원가입 요청 정보", required = true)
		@RequestBody @Valid SignUpRequestDto dto,
		HttpServletResponse response
	) {
		LoginResponseDto loginResponseDto = authService.signup(dto, response);
		return ResponseEntity.ok(loginResponseDto);
	}

	@Operation(
		summary = "소셜 로그인 (로컬 리다이렉트)",
		description = "로컬 환경용 Google OIDC 소셜 로그인 API입니다. " +
			"Google에서 발급받은 인증 코드(code)를 전달하면, " +
			"서버에서 사용자 정보를 조회하고 로그인 처리합니다. " +
			"리다이렉트 URI는 로컬 환경(localhost)으로 설정됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "소셜 로그인 성공. JWT 토큰이 헤더에 설정됩니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = LoginResponseDto.class)
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (인증 코드 오류 또는 지원하지 않는 소셜 타입)"),
		@ApiResponse(responseCode = "401", description = "소셜 로그인 인증 실패")
	})
	@PostMapping("/login/social/local")
	public ResponseEntity<LoginResponseDto> socialLoginLocal(
		@Parameter(description = "소셜 로그인 요청 정보 (인증 코드 및 소셜 타입)", required = true)
		@RequestBody @Valid SocialLoginRequestDto dto,
		HttpServletResponse response
	) {
		String redirectUri = googleOidcService.getRedirectUriLocal();
		LoginResponseDto loginResponseDto = authService.socialLogin(dto, redirectUri, response);
		return ResponseEntity.ok(loginResponseDto);
	}

	@Operation(
		summary = "소셜 로그인 (실서버 리다이렉트)",
		description = "실서버 환경용 Google OIDC 소셜 로그인 API입니다. " +
			"Google에서 발급받은 인증 코드(code)를 전달하면, " +
			"서버에서 사용자 정보를 조회하고 로그인 처리합니다. " +
			"리다이렉트 URI는 프로덕션 환경(실서버)으로 설정됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "소셜 로그인 성공. JWT 토큰이 헤더에 설정됩니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = LoginResponseDto.class)
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (인증 코드 오류 또는 지원하지 않는 소셜 타입)"),
		@ApiResponse(responseCode = "401", description = "소셜 로그인 인증 실패")
	})
	@PostMapping("/login/social/prod")
	public ResponseEntity<LoginResponseDto> socialLoginProd(
		@Parameter(description = "소셜 로그인 요청 정보 (인증 코드 및 소셜 타입)", required = true)
		@RequestBody @Valid SocialLoginRequestDto dto,
		HttpServletResponse response
	) {
		String redirectUri = googleOidcService.getRedirectUriProd();
		LoginResponseDto loginResponseDto = authService.socialLogin(dto, redirectUri, response);
		return ResponseEntity.ok(loginResponseDto);
	}

	@Operation(
		summary = "토큰 갱신",
		description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다. " +
			"기존 Access Token이 만료되었을 때 사용하며, " +
			"새로운 토큰들이 응답 헤더에 설정됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "토큰 갱신 성공. 새로운 JWT 토큰이 헤더에 설정됩니다."
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (Refresh Token 누락 또는 형식 오류)"),
		@ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 Refresh Token")
	})
	@PostMapping("/refresh")
	public ResponseEntity<Void> refresh(
		@Parameter(description = "토큰 갱신 요청 정보", required = true)
		@RequestBody @Valid RefreshTokenRequestDto dto,
		HttpServletResponse response
	) {
		authService.refreshAccessToken(dto, response);
		return ResponseEntity.ok().build();
	}

}
