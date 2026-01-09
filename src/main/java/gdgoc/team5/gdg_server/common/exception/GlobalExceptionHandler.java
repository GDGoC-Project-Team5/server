package gdgoc.team5.gdg_server.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import gdgoc.team5.gdg_server.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * BusinessException 처리
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(
		BusinessException e,
		HttpServletRequest request
	) {
		log.error("BusinessException: {}", e.getMessage(), e);
		ErrorCode errorCode = e.getErrorCode();
		ErrorResponse response = ErrorResponse.of(
			errorCode.getStatus().value(),
			errorCode.getCode(),
			e.getMessage(),
			request.getRequestURI()
		);
		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	/**
	 * Validation 실패 (@Valid, @Validated)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e,
		HttpServletRequest request
	) {
		log.error("MethodArgumentNotValidException: {}", e.getMessage());
		List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> ErrorResponse.FieldError.builder()
				.field(error.getField())
				.value(error.getRejectedValue() != null ? error.getRejectedValue().toString() : "")
				.reason(error.getDefaultMessage())
				.build())
			.collect(Collectors.toList());

		ErrorResponse response = ErrorResponse.of(
			HttpStatus.BAD_REQUEST.value(),
			ErrorCode.INVALID_INPUT_VALUE.getCode(),
			ErrorCode.INVALID_INPUT_VALUE.getMessage(),
			request.getRequestURI(),
			fieldErrors
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * BindException 처리
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(
		BindException e,
		HttpServletRequest request
	) {
		log.error("BindException: {}", e.getMessage());
		List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> ErrorResponse.FieldError.builder()
				.field(error.getField())
				.value(error.getRejectedValue() != null ? error.getRejectedValue().toString() : "")
				.reason(error.getDefaultMessage())
				.build())
			.collect(Collectors.toList());

		ErrorResponse response = ErrorResponse.of(
			HttpStatus.BAD_REQUEST.value(),
			ErrorCode.INVALID_INPUT_VALUE.getCode(),
			ErrorCode.INVALID_INPUT_VALUE.getMessage(),
			request.getRequestURI(),
			fieldErrors
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * 타입 불일치
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e,
		HttpServletRequest request
	) {
		log.error("MethodArgumentTypeMismatchException: {}", e.getMessage());
		ErrorResponse response = ErrorResponse.of(
			HttpStatus.BAD_REQUEST.value(),
			ErrorCode.INVALID_TYPE_VALUE.getCode(),
			ErrorCode.INVALID_TYPE_VALUE.getMessage(),
			request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * 지원하지 않는 HTTP Method
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e,
		HttpServletRequest request
	) {
		log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage());
		ErrorResponse response = ErrorResponse.of(
			HttpStatus.METHOD_NOT_ALLOWED.value(),
			ErrorCode.METHOD_NOT_ALLOWED.getCode(),
			ErrorCode.METHOD_NOT_ALLOWED.getMessage(),
			request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	/**
	 * 접근 거부 (권한 없음)
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(
		AccessDeniedException e,
		HttpServletRequest request
	) {
		log.error("AccessDeniedException: {}", e.getMessage());
		ErrorResponse response = ErrorResponse.of(
			HttpStatus.FORBIDDEN.value(),
			ErrorCode.HANDLE_ACCESS_DENIED.getCode(),
			ErrorCode.HANDLE_ACCESS_DENIED.getMessage(),
			request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	/**
	 * 404 Not Found
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
		NoHandlerFoundException e,
		HttpServletRequest request
	) {
		log.error("NoHandlerFoundException: {}", e.getMessage());
		ErrorResponse response = ErrorResponse.of(
			HttpStatus.NOT_FOUND.value(),
			"C006",
			"요청한 리소스를 찾을 수 없습니다.",
			request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	/**
	 * IllegalArgumentException 처리
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
		IllegalArgumentException e,
		HttpServletRequest request
	) {
		log.error("IllegalArgumentException: {}", e.getMessage(), e);
		ErrorResponse response = ErrorResponse.of(
			HttpStatus.BAD_REQUEST.value(),
			ErrorCode.INVALID_INPUT_VALUE.getCode(),
			e.getMessage(),
			request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * 그 외 모든 예외
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
		Exception e,
		HttpServletRequest request
	) {
		log.error("Exception: {}", e.getMessage(), e);
		ErrorResponse response = ErrorResponse.of(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
			ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
			request.getRequestURI()
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
