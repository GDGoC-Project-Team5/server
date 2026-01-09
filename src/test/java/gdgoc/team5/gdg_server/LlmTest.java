package gdgoc.team5.gdg_server;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

@SpringBootTest
public class LlmTest {

	private static final String API_URL = "http://localhost:8000/analyze-github";

	@Test
	void testAnalyzeGithub() {
		// RestTemplate 생성
		RestTemplate restTemplate = new RestTemplate();

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청 본문 생성
		GithubAnalyzeRequest request = new GithubAnalyzeRequest("ChooJG");

		// HttpEntity로 요청 래핑
		HttpEntity<GithubAnalyzeRequest> entity = new HttpEntity<>(request, headers);

		// API 호출
		ResponseEntity<GithubAnalyzeResponse> response = restTemplate.postForEntity(
			API_URL,
			entity,
			GithubAnalyzeResponse.class
		);

		// 응답 확인
		System.out.println("Status Code: " + response.getStatusCode());
		System.out.println("Response Body: " + response.getBody());

		// 응답 검증
		GithubAnalyzeResponse body = response.getBody();
		if (body != null) {
			System.out.println("Profile Name: " + body.profile().name());
			System.out.println("Github ID: " + body.profile().githubId());
			System.out.println("Languages: " + body.languages());
			System.out.println("Total Repos: " + body.teamworkStats().totalRepos());
			System.out.println("Strengths: " + body.analysis().strengths());
		}
	}

	// 요청 DTO
	record GithubAnalyzeRequest(String githubId) {
	}

	// 응답 DTO
	record GithubAnalyzeResponse(
		Profile profile,
		List<Language> languages,
		TeamworkStats teamworkStats,
		Analysis analysis
	) {
	}

	record Profile(
		String name,
		String githubId,
		String avatarUrl,
		String bio,
		String profileUrl,
		String updatedAt
	) {
	}

	record Language(
		String name,
		@JsonProperty("percent")
		Integer percent
	) {
	}

	record TeamworkStats(
		Integer totalRepos,
		Integer collaborativeRepos,
		Integer soloRepos
	) {
	}

	record Analysis(
		List<String> strengths,
		List<String> teamwork,
		List<String> growthFocus
	) {
	}
}
