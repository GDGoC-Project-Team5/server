package gdgoc.team5.gdg_server.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gdgoc.team5.gdg_server.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	// 제목으로 검색하면서 페이지네이션
	Page<Post> findByTitleContaining(String title, Pageable pageable);

	// 전체 게시글 페이지네이션
	Page<Post> findAll(Pageable pageable);

	// 캘린더에 표시할 게시글 조회 (특정 년/월)
	@Query("SELECT p FROM Post p WHERE p.showOnCalendar = true " +
		"AND YEAR(p.calendarDate) = :year AND MONTH(p.calendarDate) = :month " +
		"ORDER BY p.calendarDate ASC")
	List<Post> findCalendarPostsByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
