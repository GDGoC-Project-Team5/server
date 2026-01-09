package gdgoc.team5.gdg_server.post.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import gdgoc.team5.gdg_server.post.domain.Post;
import gdgoc.team5.gdg_server.post.dto.PostRequestDto;
import gdgoc.team5.gdg_server.post.dto.PostResponseDto;
import gdgoc.team5.gdg_server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	@Value("${file.upload-dir:uploads}")
	private String uploadDir;

	@Transactional
	public PostResponseDto createPost(PostRequestDto requestDto, MultipartFile file) {

		Post post = new Post(
			requestDto.title(),
			requestDto.content(),
			requestDto.author()
		);

		// 파일 처리
		if (file != null && !file.isEmpty()) {
			try {
				String savedFileName = saveFile(file);
				post.setFileName(file.getOriginalFilename());
				post.setFilePath(uploadDir + "/" + savedFileName);
				post.setFileSize(file.getSize());
			} catch (IOException e) {
				throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
			}
		}

		Post savedPost = postRepository.save(post);
		return new PostResponseDto(savedPost);
	}

	// 파일 저장 로직
	private String saveFile(MultipartFile file) throws IOException {
		// 업로드 디렉토리 생성
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		// 파일명 중복 방지 (UUID 사용)
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename != null && originalFilename.contains(".")
			? originalFilename.substring(originalFilename.lastIndexOf("."))
			: "";
		String savedFilename = UUID.randomUUID().toString() + extension;

		// 파일 저장
		Path filePath = uploadPath.resolve(savedFilename);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		return savedFilename;
	}

	// 파일 다운로드
	public ResponseEntity<Resource> downloadFile(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + id));

		if (post.getFilePath() == null) {
			throw new IllegalArgumentException("첨부파일이 없습니다.");
		}

		try {
			Path filePath = Paths.get(post.getFilePath());
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists()) {
				throw new RuntimeException("파일을 찾을 수 없습니다.");
			}

			return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + post.getFileName() + "\"")
				.body(resource);
		} catch (Exception e) {
			throw new RuntimeException("파일 다운로드 실패: " + e.getMessage());
		}
	}

	// 단일 게시물 조회 기능 (+조회수 증가)
	@Transactional
	public PostResponseDto getPostById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));

		// 조회수 증가
		post.setViews(post.getViews() + 1);

		return new PostResponseDto(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponseDto> getAllPosts() {
		return postRepository.findAll().stream()
			.map(PostResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public void deletePost(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + id));

		// 파일도 함께 삭제
		if (post.getFilePath() != null) {
			try {
				Files.deleteIfExists(Paths.get(post.getFilePath()));
			} catch (IOException e) {
				System.err.println("파일 삭제 실패: " + e.getMessage());
			}
		}

		postRepository.delete(post);
	}
}