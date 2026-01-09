package gdgoc.team5.gdg_server.post.controller.response;

import java.time.LocalDateTime;

import gdgoc.team5.gdg_server.post.domain.Post;

public record PostListResponseDto(
	Long postId,
	Long views,
	String title,
	String realName,
	Boolean hasFile,
	LocalDateTime createdAt
) {
	public static PostListResponseDto fromDomain(Post post, Boolean hasFile, String realName) {
		return new PostListResponseDto(
			post.getId(),
			post.getViews(),
			post.getTitle(),
			realName,
			hasFile,
			post.getCreatedAt()
		);
	}
}
