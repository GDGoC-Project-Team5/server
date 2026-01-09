package gdgoc.team5.gdg_server.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdgoc.team5.gdg_server.profile.controller.request.ProfileRequestDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileResponseDto;
import gdgoc.team5.gdg_server.profile.domain.Profile;
import gdgoc.team5.gdg_server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

	private final ProfileRepository profileRepository;

	// 프로필 수정 또는 생성
	@Transactional
	public ProfileResponseDto upsertProfile(Long memberId, ProfileRequestDto requestDto) {
		// memberId로 프로필 조회
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElse(null);

		if (profile != null) {
			// 프로필이 존재하면 업데이트
			profile.updateProfile(
				requestDto.department(),
				requestDto.bio(),
				requestDto.memberRole(),
				requestDto.generation(),
				requestDto.part(),
				requestDto.skills(),
				requestDto.snsLink1(),
				requestDto.snsLink2(),
				requestDto.snsLink3()
			);
		} else {
			// 프로필이 없으면 새로 생성
			profile = Profile.createProfile(
				memberId,
				requestDto.department(),
				requestDto.bio(),
				requestDto.memberRole(),
				requestDto.generation(),
				requestDto.part(),
				requestDto.skills(),
				requestDto.snsLink1(),
				requestDto.snsLink2(),
				requestDto.snsLink3()
			);
			profileRepository.save(profile);
		}

		return ProfileResponseDto.fromDomain(profile);
	}

	// 프로필 조회
	public ProfileResponseDto getProfile(Long memberId) {
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다: memberId=" + memberId));

		return ProfileResponseDto.fromDomain(profile);
	}
}
