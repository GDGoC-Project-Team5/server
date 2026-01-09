package gdgoc.team5.gdg_server.profile.controller.response;

import java.time.LocalDateTime;

import gdgoc.team5.gdg_server.profile.domain.MemberRole;
import gdgoc.team5.gdg_server.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 조회/수정 응답")
public record ProfileResponseDto(
	@Schema(description = "프로필 ID", example = "1")
	Long profileId,

	@Schema(description = "회원 ID", example = "5")
	Long memberId,

	@Schema(description = "학과", example = "컴퓨터공학과")
	String department,

	@Schema(description = "자기소개", example = "안녕하세요! GDG에서 백엔드 개발을 담당하고 있습니다.")
	String bio,

	@Schema(description = "운영진 역할 (MEMBER: 일반 회원, CORE: 운영진, LEAD: 리드)", example = "MEMBER")
	MemberRole memberRole,

	@Schema(description = "기수", example = "5")
	Integer generation,

	@Schema(description = "파트", example = "Backend")
	String part,

	@Schema(description = "보유 스킬", example = "Java, Spring Boot, MySQL")
	String skills,

	@Schema(description = "SNS 링크 1", example = "https://github.com/username")
	String snsLink1,

	@Schema(description = "SNS 링크 2", example = "https://linkedin.com/in/username")
	String snsLink2,

	@Schema(description = "SNS 링크 3", example = "https://instagram.com/username")
	String snsLink3,

	@Schema(description = "프로필 생성일시", example = "2024-01-10T14:30:00")
	LocalDateTime createdAt,

	@Schema(description = "프로필 수정일시", example = "2024-01-11T10:20:00")
	LocalDateTime updatedAt
) {
	public static ProfileResponseDto fromDomain(Profile profile) {
		return new ProfileResponseDto(
			profile.getId(),
			profile.getMemberId(),
			profile.getDepartment(),
			profile.getBio(),
			profile.getMemberRole(),
			profile.getGeneration(),
			profile.getPart(),
			profile.getSkills(),
			profile.getSnsLink1(),
			profile.getSnsLink2(),
			profile.getSnsLink3(),
			profile.getCreatedAt(),
			profile.getUpdatedAt()
		);
	}
}
