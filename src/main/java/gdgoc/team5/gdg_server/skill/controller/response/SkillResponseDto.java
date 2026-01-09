package gdgoc.team5.gdg_server.skill.controller.response;

import gdgoc.team5.gdg_server.skill.domain.Skill;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기술 스택 응답")
public record SkillResponseDto(
	@Schema(description = "기술 스택 ID", example = "1")
	Long id,

	@Schema(description = "기술 스택 이름", example = "Java")
	String name,

	@Schema(description = "카테고리 (Language, Framework, Database, Tool 등)", example = "Language")
	String category
) {
	public static SkillResponseDto fromDomain(Skill skill) {
		return new SkillResponseDto(
			skill.getId(),
			skill.getName(),
			skill.getCategory()
		);
	}
}
