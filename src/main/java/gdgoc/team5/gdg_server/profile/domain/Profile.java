package gdgoc.team5.gdg_server.profile.domain;

import gdgoc.team5.gdg_server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Profile extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id")
	private Long id;

	@Column(nullable = false, unique = true)
	private Long memberId;  // 회원 ID (FK)

	@Column(length = 100)
	private String department;  // 학과

	@Column(columnDefinition = "TEXT")
	private String bio;  // 자기소개

	@Enumerated(EnumType.STRING)
	private MemberRole memberRole; // 운영진 여부

	private Integer generation;  // 기수

	@Column(length = 50)
	private String part;  // 파트 (예: Frontend, Backend, Design 등)

	@Column(columnDefinition = "TEXT")
	private String skills;  // 스킬 (쉼표로 구분된 문자열)

	@Column(length = 500)
	private String snsLink1;  // SNS 링크 1 (예: GitHub)

	@Column(length = 500)
	private String snsLink2;  // SNS 링크 2 (예: LinkedIn)

	@Column(length = 500)
	private String snsLink3;  // SNS 링크 3 (예: Instagram)

}
