package gdgoc.team5.gdg_server.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gdgoc.team5.gdg_server.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	// memberId로 프로필 조회
	Optional<Profile> findByMemberId(Long memberId);

	// 기수별 프로필 조회
	List<Profile> findByGeneration(Integer generation);

	// 전체 프로필 조회 (기수 오름차순)
	List<Profile> findAllByOrderByGenerationAsc();
}
