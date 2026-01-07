package gdgoc.team5.gdg_server.post.dto;

import gdgoc.team5.gdg_server.post.domain.Post;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        Long views,
        String title,
        String content,
        String author,
        LocalDateTime createdDate
) {
    public PostResponseDto(Post post) {
        this(
                post.getId(),
                post.getViews(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedDate()
        );
    }
}
