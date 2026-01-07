package gdgoc.team5.gdg_server.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDateTime createdDate;

    //댓글은 지금 필요없을듯?
    //@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comment> comments = new ArrayList<>();

    public Post(Long id, Long views, String title, String content, String author, LocalDateTime createdDate) {
        this.id = id;
        this.views = views != null ? views : 0L;
        this.title = title;
        this.content = content;
        this.author = author;
    }
    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.views = 0L;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}
