package gdgoc.team5.gdg_server.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gdgoc.team5.gdg_server.auth.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUsername(String username);

	Optional<Member> findBySocialId(String socialId);
}
