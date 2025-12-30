package gdgoc.team5.gdg_server.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gdgoc.team5.gdg_server.auth.controller.request.SignUpRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.TestLoginRequestDto;
import gdgoc.team5.gdg_server.auth.controller.response.LoginResponseDto;
import gdgoc.team5.gdg_server.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login/test")
	@Operation(summary = "테스트 로그인", description = "존재하지 않는 username 입력시 회원가입, 존재하는 username 입력시 로그인")
	public ResponseEntity<LoginResponseDto> testLogin(
		@RequestBody @Valid TestLoginRequestDto dto,
		HttpServletResponse response
	) {
		LoginResponseDto loginResponseDto = authService.testLogin(dto, response);
		return ResponseEntity.ok(loginResponseDto);
	}

	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "memberRole: 멤버, 코어, 리드 구분 / memberType: 로그인 형식 구분 (구글, 로컬 등)")
	public ResponseEntity<LoginResponseDto> signup(
		@RequestBody @Valid SignUpRequestDto dto,
		HttpServletResponse response
	) {
		LoginResponseDto loginResponseDto = authService.signup(dto, response);
		return ResponseEntity.ok(loginResponseDto);
	}

}
