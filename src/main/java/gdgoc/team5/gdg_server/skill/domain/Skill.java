package gdgoc.team5.gdg_server.skill.domain;

import gdgoc.team5.gdg_server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Skill extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "skill_id")
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String name;  // 기술 스택 이름 (예: Java, React, Spring Boot)

	@Column(length = 50)
	private String category;  // 카테고리 (예: Language, Framework, Database, Tool)

	public static Skill create(String name, String category) {
		return Skill.builder()
			.name(name)
			.category(category)
			.build();
	}
}
