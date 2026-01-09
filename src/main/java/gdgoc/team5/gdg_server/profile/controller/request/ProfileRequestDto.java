package gdgoc.team5.gdg_server.profile.controller.request;

import gdgoc.team5.gdg_server.profile.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 수정 요청")
public record ProfileRequestDto(
	@Schema(description = "학과", example = "컴퓨터공학과")
	String department,

	@Schema(description = "자기소개", example = "안녕하세요! GDG에서 백엔드 개발을 담당하고 있습니다.")
	String bio,

	@Schema(description = "운영진 역할 (MEMBER: 일반 회원, CORE: 운영진, LEAD: 리드)", example = "MEMBER")
	MemberRole memberRole,

	@Schema(description = "기수", example = "5")
	Integer generation,

	@Schema(description = "파트 (Frontend, Backend, Design, PM 등)", example = "Backend")
	String part,

	@Schema(description = "보유 스킬 (쉼표로 구분)", example = "Java, Spring Boot, MySQL")
	String skills,

	@Schema(description = "SNS 링크 1 (예: GitHub)", example = "https://github.com/username")
	String snsLink1,

	@Schema(description = "SNS 링크 2 (예: LinkedIn)", example = "https://linkedin.com/in/username")
	String snsLink2,

	@Schema(description = "SNS 링크 3 (예: Instagram)", example = "https://instagram.com/username")
	String snsLink3
) {
}
