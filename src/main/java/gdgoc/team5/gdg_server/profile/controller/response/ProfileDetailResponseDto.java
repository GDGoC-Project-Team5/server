package gdgoc.team5.gdg_server.profile.controller.response;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 멤버 프로필 상세 조회 응답")
public record ProfileDetailResponseDto(
	@Schema(description = "회원 ID", example = "1")
	Long memberId,

	@Schema(description = "실명", example = "홍길동")
	String realName,

	@Schema(description = "이메일", example = "hong@example.com")
	String email,

	@Schema(description = "학과", example = "컴퓨터공학과")
	String department,

	@Schema(description = "한줄소개", example = "안녕하세요! GDG에서 백엔드 개발을 담당하고 있습니다.")
	String bio,

	@Schema(description = "보유 스킬 (쉼표로 구분)", example = "Java, Spring Boot, MySQL")
	String skills,

	@Schema(description = "SNS 링크 1 (GitHub 등)", example = "https://github.com/username")
	String snsLink1,

	@Schema(description = "SNS 링크 2 (LinkedIn 등)", example = "https://linkedin.com/in/username")
	String snsLink2,

	@Schema(description = "SNS 링크 3 (Instagram 등)", example = "https://instagram.com/username")
	String snsLink3,

	@Schema(description = "프로필 이미지 URL (현재 null, 추후 구현 예정)", example = "null")
	String profileImageUrl
) {
	public static ProfileDetailResponseDto fromDomain(Member member, Profile profile) {
		return new ProfileDetailResponseDto(
			member.getId(),
			member.getRealName(),
			member.getEmail(),
			profile.getDepartment(),
			profile.getBio(),
			profile.getSkills(),
			profile.getSnsLink1(),
			profile.getSnsLink2(),
			profile.getSnsLink3(),
			null
		);
	}
}
