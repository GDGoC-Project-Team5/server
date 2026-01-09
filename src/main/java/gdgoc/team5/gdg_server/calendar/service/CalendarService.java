package gdgoc.team5.gdg_server.calendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.calendar.controller.response.CalendarResponseDto;
import gdgoc.team5.gdg_server.post.domain.Post;
import gdgoc.team5.gdg_server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	// 특정 년/월의 캘린더 일정 조회
	public List<CalendarResponseDto> getCalendarEvents(int year, int month) {
		// showOnCalendar = true이고 해당 년/월에 속하는 게시글 조회
		List<Post> posts = postRepository.findCalendarPostsByYearAndMonth(year, month);

		// Post를 CalendarResponseDto로 변환
		return posts.stream()
			.map(post -> {
				// memberId로 작성자 정보 조회
				Member member = memberRepository.findById(post.getMemberId())
					.orElseThrow(() -> new IllegalArgumentException(
						"작성자를 찾을 수 없습니다: memberId=" + post.getMemberId()));

				return CalendarResponseDto.fromDomain(post, member.getRealName());
			})
			.collect(Collectors.toList());
	}
}
