package gdgoc.team5.gdg_server.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.repository.MemberRepository;
import gdgoc.team5.gdg_server.common.component.LocalFileUploadComponent;
import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import gdgoc.team5.gdg_server.file.domain.File;
import gdgoc.team5.gdg_server.file.repository.FileRepository;
import gdgoc.team5.gdg_server.post.controller.request.PostRequestDto;
import gdgoc.team5.gdg_server.post.controller.response.PostListResponseDto;
import gdgoc.team5.gdg_server.post.controller.response.PostResponseDto;
import gdgoc.team5.gdg_server.post.domain.Post;
import gdgoc.team5.gdg_server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final FileRepository fileRepository;
	private final LocalFileUploadComponent localFileUploadComponent;

	@Transactional
	public PostResponseDto createPost(PostRequestDto requestDto, TokenInfo tokenInfo) {
		Post post = Post.createPost(requestDto, tokenInfo.memberId());
		Optional<Member> member = memberRepository.findById(tokenInfo.memberId());
		return PostResponseDto.fromDomain(postRepository.save(post), member.get().getRealName());
	}

	@Transactional
	public void uploadFiles(Long postId, List<MultipartFile> files) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + postId));

		if (files == null || files.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}

		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				String fileUrl = localFileUploadComponent.upload(file, "posts");

				File fileEntity = File.builder()
					.post(post)
					.fileUrl(fileUrl)
					.originalFileName(file.getOriginalFilename())
					.fileSize(file.getSize())
					.contentType(file.getContentType())
					.build();

				fileRepository.save(fileEntity);
				log.info("파일 업로드 성공: {} (게시글 ID: {})", file.getOriginalFilename(), post.getId());
			}
		}
	}

	// 단일 게시물 조회 기능 (+조회수 증가)
	@Transactional
	public PostResponseDto getPostById(Long id) {
		Post post = postRepository.findByIdWithFiles(id)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));

		// 조회수 증가
		post.setViews(post.getViews() + 1);

		// memberId로 작성자 정보 조회
		Member member = memberRepository.findById(post.getMemberId())
			.orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다: " + post.getMemberId()));

		return PostResponseDto.fromDomain(post, member.getRealName());
	}

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

	// hasFile 판단 로직
	private Boolean checkHasFile(Post post) {
		try {
			return post.getFiles() != null && !post.getFiles().isEmpty();
		} catch (Exception e) {
			// LAZY 로딩 실패 시 파일 존재 여부를 별도 조회
			return !fileRepository.findByPostId(post.getId()).isEmpty();
		}
	}

	@Transactional
	public void deletePost(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));

		// 게시글에 연결된 파일 조회
		List<File> files = fileRepository.findByPostId(id);

		// 각 파일을 파일 시스템에서 삭제 (실패해도 게시글 삭제는 진행)
		for (File file : files) {
			try {
				localFileUploadComponent.delete(file.getFileUrl());
				log.info("파일 삭제 성공: {} (게시글 ID: {})", file.getOriginalFileName(), id);
			} catch (Exception e) {
				log.warn("파일 삭제 실패 (계속 진행): {} (게시글 ID: {})", file.getOriginalFileName(), id, e);
			}
		}

		// 게시글 삭제 (cascade로 인해 File 엔티티도 함께 삭제됨)
		postRepository.delete(post);
	}
}