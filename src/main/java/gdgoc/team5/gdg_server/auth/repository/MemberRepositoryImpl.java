package gdgoc.team5.gdg_server.auth.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import gdgoc.team5.gdg_server.auth.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

	private final MemberJpaRepository memberJpaRepository;

	public Optional<Member> findByUsername(String username) {
		return memberJpaRepository.findByUsername(username);
	}

	@Override
	public Optional<Member> findBySocialId(String socialId) {
		return memberJpaRepository.findBySocialId(socialId);
	}

	public Member save(Member member) {
		return memberJpaRepository.save(member);
	}

	@Override
	public Optional<Member> findById(Long memberId) {
		return memberJpaRepository.findById(memberId);
	}
}
