package gdgoc.team5.gdg_server.post.repository;

import gdgoc.team5.gdg_server.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
