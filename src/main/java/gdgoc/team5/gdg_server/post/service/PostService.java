package gdgoc.team5.gdg_server.post.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import gdgoc.team5.gdg_server.post.controller.request.PostRequestDto;
import gdgoc.team5.gdg_server.post.controller.response.PostListResponseDto;
import gdgoc.team5.gdg_server.post.domain.Post;
import gdgoc.team5.gdg_server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Value("${file.upload-dir:uploads}")
	private String uploadDir;

	@Transactional
	public void createPost(PostRequestDto requestDto, TokenInfo tokenInfo) {
		Post post = Post.createPost(requestDto, tokenInfo.memberId());
		postRepository.save(post);
	}

	// 단일 게시물 조회 기능 (+조회수 증가)
	@Transactional
	public PostListResponseDto getPostById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));

		// 조회수 증가
		post.setViews(post.getViews() + 1);
		return null;
	}

	@Transactional(readOnly = true)
	public Page<PostListResponseDto> getAllPosts(Pageable pageable, String title) {
		Page<Post> posts;

		// 제목 검색 여부에 따라 다른 메서드 호출
		if (title != null && !title.isBlank()) {
			posts = postRepository.findByTitleContaining(title, pageable);
		} else {
			posts = postRepository.findAll(pageable);
		}

		// Post를 PostListResponseDto로 변환
		return posts.map(post -> {
			// memberId로 작성자 정보 조회
			Member member = memberRepository.findById(post.getMemberId())
				.orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다: " + post.getMemberId()));

			// hasFile은 현재 false로 고정
			Boolean hasFile = checkHasFile(post);

			return PostListResponseDto.fromDomain(post, hasFile, member.getRealName());
		});
	}

	// hasFile 판단 로직 (나중에 구현 예정)
	private Boolean checkHasFile(Post post) {
		return Boolean.FALSE;
	}

	@Transactional
	public void deletePost(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));

		postRepository.delete(post);
	}
}