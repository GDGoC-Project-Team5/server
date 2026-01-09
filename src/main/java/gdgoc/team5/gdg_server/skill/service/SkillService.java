package gdgoc.team5.gdg_server.skill.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdgoc.team5.gdg_server.skill.controller.response.SkillResponseDto;
import gdgoc.team5.gdg_server.skill.domain.Skill;
import gdgoc.team5.gdg_server.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillService {

	private final SkillRepository skillRepository;

	// 기술 스택 검색
	public List<SkillResponseDto> searchSkills(String keyword) {
		List<Skill> skills;

		if (keyword != null && !keyword.isBlank()) {
			// 키워드가 있으면 검색
			skills = skillRepository.findByNameContainingIgnoreCase(keyword);
		} else {
			// 키워드가 없으면 전체 조회
			skills = skillRepository.findAllByOrderByNameAsc();
		}

		return skills.stream()
			.map(SkillResponseDto::fromDomain)
			.collect(Collectors.toList());
	}
}
