package gdgoc.team5.gdg_server.auth.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import gdgoc.team5.gdg_server.auth.domain.Member;

@Repository
public interface MemberRepository {

	Optional<Member> findByUsername(String username);

	Member save(Member member);

	Optional<Member> findById(Long memberId);
}
