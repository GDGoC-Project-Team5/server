package gdgoc.team5.gdg_server.post.dto;

import java.time.LocalDateTime;

import gdgoc.team5.gdg_server.post.domain.Post;

public record PostResponseDto(
	Long id,
	Long views,
	String title,
	String content,
	String author,
	String fileName,
	String filePath,
	Long fileSize,
	LocalDateTime createdDate
) {
	public PostResponseDto(Post post) {
		this(
			post.getId(),
			post.getViews(),
			post.getTitle(),
			post.getContent(),
			post.getAuthor(),
			post.getFileName(),
			post.getFilePath(),
			post.getFileSize(),
			post.getCreatedDate()
		);
	}
}
