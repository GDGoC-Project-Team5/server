package gdgoc.team5.gdg_server.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gdgoc.team5.gdg_server.file.domain.File;

public interface FileRepository extends JpaRepository<File, Long> {

	List<File> findByPostId(Long postId);
}
