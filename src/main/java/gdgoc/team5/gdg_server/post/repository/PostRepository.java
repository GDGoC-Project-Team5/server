package gdgoc.team5.gdg_server.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gdgoc.team5.gdg_server.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	// 제목으로 검색하면서 페이지네이션
	Page<Post> findByTitleContaining(String title, Pageable pageable);

	// 전체 게시글 페이지네이션
	Page<Post> findAll(Pageable pageable);
}
