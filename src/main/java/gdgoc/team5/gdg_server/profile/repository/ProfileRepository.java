package gdgoc.team5.gdg_server.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gdgoc.team5.gdg_server.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	// memberId로 프로필 조회
	Optional<Profile> findByMemberId(Long memberId);
}
