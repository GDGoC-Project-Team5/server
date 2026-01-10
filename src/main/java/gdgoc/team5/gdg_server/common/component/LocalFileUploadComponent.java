package gdgoc.team5.gdg_server.common.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LocalFileUploadComponent {

	@Value("${file.upload.path}")
	private String uploadPath;

	@Value("${file.upload.url-prefix}")
	private String urlPrefix;

	@Value("${server.port:8080}")
	private String serverPort;

	@PostConstruct
	public void init() {
		try {
			Path uploadDir = Paths.get(uploadPath);
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
				log.info("업로드 디렉터리 생성: {}", uploadPath);
			}
		} catch (IOException e) {
			log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
			throw new RuntimeException("업로드 디렉터리 생성 실패", e);
		}
	}

	/**
	 * 로컬 디렉터리에 파일 업로드
	 * @param file 업로드할 파일
	 * @param dirName 디렉토리명 (예: "posts", "profiles")
	 * @return 업로드된 파일의 URL
	 */
	public String upload(MultipartFile file, String dirName) {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
		}

		String fileName = createFileName(file.getOriginalFilename(), dirName);
		Path targetPath = Paths.get(uploadPath, fileName);

		try {
			Path dirPath = targetPath.getParent();
			if (!Files.exists(dirPath)) {
				Files.createDirectories(dirPath);
			}

			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
			log.info("파일 업로드 성공: {}", fileName);

			return getFileUrl(fileName);
		} catch (IOException e) {
			log.error("파일 업로드 실패: {}", e.getMessage());
			throw new RuntimeException("파일 업로드에 실패했습니다.", e);
		}
	}

	/**
	 * 로컬 디렉터리에서 파일 삭제
	 * @param fileUrl 삭제할 파일의 URL
	 */
	public void delete(String fileUrl) {
		String fileName = extractFileNameFromUrl(fileUrl);
		Path filePath = Paths.get(uploadPath, fileName);

		try {
			if (Files.exists(filePath)) {
				Files.delete(filePath);
				log.info("파일 삭제 성공: {}", fileName);
			} else {
				log.warn("삭제할 파일이 존재하지 않습니다: {}", fileName);
			}
		} catch (IOException e) {
			log.error("파일 삭제 실패: {}", e.getMessage());
			throw new RuntimeException("파일 삭제에 실패했습니다.", e);
		}
	}

	/**
	 * 고유한 파일명 생성
	 * @param originalFileName 원본 파일명
	 * @param dirName 디렉토리명
	 * @return 생성된 파일명 (디렉토리/UUID_원본파일명)
	 */
	private String createFileName(String originalFileName, String dirName) {
		String uuid = UUID.randomUUID().toString();
		String extension = extractExtension(originalFileName);
		return dirName + "/" + uuid + extension;
	}

	/**
	 * 파일 확장자 추출
	 * @param fileName 파일명
	 * @return 확장자 (.포함)
	 */
	private String extractExtension(String fileName) {
		if (fileName == null || !fileName.contains(".")) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * URL에서 파일명 추출
	 * @param fileUrl 파일 URL
	 * @return 파일명
	 */
	private String extractFileNameFromUrl(String fileUrl) {
		if (fileUrl.contains(urlPrefix)) {
			return fileUrl.substring(fileUrl.indexOf(urlPrefix) + urlPrefix.length() + 1);
		}
		return fileUrl;
	}

	/**
	 * 파일 URL 생성
	 * @param fileName 파일명
	 * @return 파일 접근 URL
	 */
	private String getFileUrl(String fileName) {
		return urlPrefix + "/" + fileName;
	}
}
