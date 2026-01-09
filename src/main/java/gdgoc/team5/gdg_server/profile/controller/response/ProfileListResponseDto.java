package gdgoc.team5.gdg_server.profile.controller.response;

import gdgoc.team5.gdg_server.profile.domain.MemberRole;
import gdgoc.team5.gdg_server.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 목록 조회 응답 (리스트용)")
public record ProfileListResponseDto(
	@Schema(description = "회원 ID", example = "1")
	Long memberId,

	@Schema(description = "실명", example = "홍길동")
	String realName,

	@Schema(description = "운영진 역할 (MEMBER: 일반 회원, CORE: 운영진, LEAD: 리드)", example = "CORE")
	MemberRole memberRole,

	@Schema(description = "기수", example = "5")
	Integer generation,

	@Schema(description = "파트 (Frontend, Backend, Design, PM 등)", example = "Backend")
	String part,

	@Schema(description = "SNS 링크 1 (GitHub 등)", example = "https://github.com/username")
	String snsLink1,

	@Schema(description = "SNS 링크 2 (LinkedIn 등)", example = "https://linkedin.com/in/username")
	String snsLink2,

	@Schema(description = "SNS 링크 3 (Instagram 등)", example = "https://instagram.com/username")
	String snsLink3,

	@Schema(description = "프로필 이미지 URL (현재 null, 추후 구현 예정)", example = "null")
	String profileImageUrl
) {
	public static ProfileListResponseDto fromDomain(Profile profile, String realName) {
		return new ProfileListResponseDto(
			profile.getMemberId(),
			realName,
			profile.getMemberRole(),
			profile.getGeneration(),
			profile.getPart(),
			profile.getSnsLink1(),
			profile.getSnsLink2(),
			profile.getSnsLink3(),
			null  // 프로필 이미지는 추후 구현
		);
	}
}
