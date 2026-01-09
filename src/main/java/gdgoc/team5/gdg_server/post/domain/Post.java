package gdgoc.team5.gdg_server.post.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long views = 0L;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(nullable = false)
	private String author;

	private String fileName;        // 원본 파일명

	private String filePath;        // 저장된 파일 경로

	private Long fileSize;          // 파일 크기

	private LocalDateTime createdDate;

	public Post(Long id, String title, String content, String author, Long views, String fileName,
		String filePath, Long fileSize, LocalDateTime createdDate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.views = views != null ? views : 0L;
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.createdDate = createdDate;
	}

	public Post(String title, String content, String author, Long views) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.views = views;
	}

	public Post(String title, String content, String author) {
	}

	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}
}
