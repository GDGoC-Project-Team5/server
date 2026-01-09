package gdgoc.team5.gdg_server.common.exception.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

	private int status;
	private String error;
	private String message;
	private String path;
	private LocalDateTime timestamp;
	private List<FieldError> errors;

	public static ErrorResponse of(int status, String error, String message, String path) {
		return ErrorResponse.builder()
			.status(status)
			.error(error)
			.message(message)
			.path(path)
			.timestamp(LocalDateTime.now())
			.build();
	}

	public static ErrorResponse of(int status, String error, String message, String path, List<FieldError> errors) {
		return ErrorResponse.builder()
			.status(status)
			.error(error)
			.message(message)
			.path(path)
			.timestamp(LocalDateTime.now())
			.errors(errors)
			.build();
	}

	@Getter
	@Builder
	public static class FieldError {
		private String field;
		private String value;
		private String reason;
	}
}
