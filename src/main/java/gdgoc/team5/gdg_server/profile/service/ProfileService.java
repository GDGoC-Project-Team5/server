package gdgoc.team5.gdg_server.profile.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.common.component.LocalFileUploadComponent;
import gdgoc.team5.gdg_server.profile.controller.request.GithubAnalyzeRequestDto;
import gdgoc.team5.gdg_server.profile.controller.request.ProfileRequestDto;
import gdgoc.team5.gdg_server.profile.controller.response.GithubAnalyzeResponseDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileDetailResponseDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileListResponseDto;
import gdgoc.team5.gdg_server.profile.controller.response.ProfileResponseDto;
import gdgoc.team5.gdg_server.profile.domain.Profile;
import gdgoc.team5.gdg_server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final MemberRepository memberRepository;
	private final LocalFileUploadComponent localFileUploadComponent;

	private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB

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

	// 기수별 멤버 목록 조회
	public List<ProfileListResponseDto> getMembersByGeneration(Integer generation) {
		List<Profile> profiles;

		// 특정 기수 조회
		profiles = profileRepository.findByGeneration(generation);

		// Profile을 ProfileListResponseDto로 변환
		return profiles.stream()
			.map(profile -> {
				// memberId로 Member 조회하여 realName 가져오기
				Member member = memberRepository.findById(profile.getMemberId())
					.orElseThrow(() -> new IllegalArgumentException(
						"회원을 찾을 수 없습니다: memberId=" + profile.getMemberId()));

				return ProfileListResponseDto.fromDomain(profile, member.getRealName());
			})
			.collect(Collectors.toList());
	}

	// 특정 멤버의 프로필 상세 조회
	public ProfileDetailResponseDto getMemberProfile(Long memberId) {
		// Member 조회
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: memberId=" + memberId));

		// Profile 조회
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다: memberId=" + memberId));

		return ProfileDetailResponseDto.fromDomain(member, profile);
	}

	// GitHub 분석 API 호출
	public GithubAnalyzeResponseDto analyzeGithub(String githubId) {
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8000/analyze-github";

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청 본문 생성
		GithubAnalyzeRequestDto request = new GithubAnalyzeRequestDto(githubId);
		HttpEntity<GithubAnalyzeRequestDto> entity = new HttpEntity<>(request, headers);

		try {
			// API 호출
			ResponseEntity<GithubAnalyzeResponseDto> response = restTemplate.postForEntity(
				apiUrl,
				entity,
				GithubAnalyzeResponseDto.class
			);

			// 200 응답인 경우
			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody();
			} else {
				throw new IllegalStateException("GitHub 분석 API 호출 실패: 상태 코드=" + response.getStatusCode());
			}

		} catch (HttpClientErrorException e) {
			// 400번대 에러
			throw new IllegalArgumentException(
				"GitHub 분석 요청이 잘못되었습니다. (상태 코드: " + e.getStatusCode() + ")", e);

		} catch (HttpServerErrorException e) {
			// 500번대 에러
			throw new IllegalStateException(
				"GitHub 분석 서버에 문제가 발생했습니다. (상태 코드: " + e.getStatusCode() + ")", e);

		} catch (Exception e) {
			// 기타 에러 (네트워크 오류 등)
			throw new IllegalStateException("GitHub 분석 API 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	// 프로필 이미지 업로드
	@Transactional
	public String updateProfileImage(Long memberId, MultipartFile image) {
		// 파일 크기 검증 (2MB 제한)
		if (image.getSize() > MAX_IMAGE_SIZE) {
			throw new IllegalArgumentException("이미지 크기는 2MB를 초과할 수 없습니다.");
		}

		// 이미지 파일 형식 검증
		String contentType = image.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
		}

		// 프로필 조회 (없으면 생성)
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseGet(() -> {
				Profile newProfile = Profile.createProfile(
					memberId, null, null, null, null, null, null, null, null, null
				);
				return profileRepository.save(newProfile);
			});

		// 기존 이미지가 있다면 삭제
		if (profile.getProfileImageUrl() != null) {
			try {
				localFileUploadComponent.delete(profile.getProfileImageUrl());
			} catch (Exception e) {
				// 기존 파일 삭제 실패는 로그만 남기고 진행
			}
		}

		// 새 이미지 업로드
		String imageUrl = localFileUploadComponent.upload(image, "profiles");

		// 프로필 이미지 URL 업데이트
		profile.updateProfileImage(imageUrl);

		return imageUrl;
	}
}
