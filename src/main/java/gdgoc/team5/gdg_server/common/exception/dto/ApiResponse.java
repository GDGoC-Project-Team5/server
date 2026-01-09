package gdgoc.team5.gdg_server.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

	private boolean success;
	private String message;
	private T data;

	// 성공 응답 (데이터 있음)
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, "Success", data);
	}

	// 성공 응답 (데이터 없음)
	public static <T> ApiResponse<T> success(String message) {
		return new ApiResponse<>(true, message, null);
	}

	// 성공 응답 (메시지 + 데이터)
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	// 실패 응답
	public static <T> ApiResponse<T> fail(String message) {
		return new ApiResponse<>(false, message, null);
	}

	// 실패 응답 (데이터 포함)
	public static <T> ApiResponse<T> fail(String message, T data) {
		return new ApiResponse<>(false, message, data);
	}
}
