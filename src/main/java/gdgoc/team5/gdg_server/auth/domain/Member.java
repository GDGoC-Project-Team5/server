package gdgoc.team5.gdg_server.auth.domain;

import gdgoc.team5.gdg_server.auth.controller.request.SignUpRequestDto;
import gdgoc.team5.gdg_server.auth.controller.request.TestLoginRequestDto;
import gdgoc.team5.gdg_server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String realName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberType memberType;

	@Column(nullable = false)
	private Boolean isPending;        // 승인 여부

	@Column(nullable = false)
	@Builder.Default
	private Boolean isAdmin = false;

	// 회원가입
	public Member(SignUpRequestDto dto) {
		this.username = dto.username();
		this.email = dto.email();
		this.realName = dto.realName();
		this.memberType = dto.memberType();
		this.isPending = false;
		this.isAdmin = false;
	}

	// 테스트 유저 회원가입
	public static Member testSignUp(TestLoginRequestDto dto) {
		return Member.builder()
			.username(dto.username())
			.realName(dto.username())
			.memberType(MemberType.TEST)
			.email(dto.username() + "@test.com")
			.isPending(Boolean.TRUE)
			.isAdmin(dto.isAdmin())
			.build();
	}
}
