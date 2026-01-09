package gdgoc.team5.gdg_server.skill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gdgoc.team5.gdg_server.skill.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

	// 기술 스택 이름으로 검색 (대소문자 구분 없이)
	List<Skill> findByNameContainingIgnoreCase(String name);

	// 모든 기술 스택 조회 (이름 오름차순)
	List<Skill> findAllByOrderByNameAsc();
}
