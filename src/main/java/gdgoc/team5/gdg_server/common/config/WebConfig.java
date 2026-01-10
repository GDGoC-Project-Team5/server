package gdgoc.team5.gdg_server.common.config;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfoArgumentResolver;
import gdgoc.team5.gdg_server.common.controller.security.AdminCheckInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AdminCheckInterceptor adminCheckInterceptor;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Value("${file.upload.url-prefix}")
	private String urlPrefix;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		CorsConfiguration config = CorsConfig.corsConfiguration();
		registry
			.addMapping("/**")
			.allowedOrigins(Objects.requireNonNull(config.getAllowedOrigins()).toArray(new String[0]))
			.allowedMethods(Objects.requireNonNull(config.getAllowedMethods()).toArray(new String[0]))
			.allowedHeaders(Objects.requireNonNull(config.getAllowedHeaders()).toArray(new String[0]))
			.exposedHeaders(Objects.requireNonNull(config.getExposedHeaders()).toArray(new String[0]))
			.allowCredentials(Boolean.TRUE.equals(config.getAllowCredentials()))
			.allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드 지정
			.allowedHeaders("*") // 모든 헤더 허용
			.allowCredentials(true) // 자격 증명 허용 여부
			.maxAge(3600); // preflight 요청 캐시 시간
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new TokenInfoArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminCheckInterceptor)
			.addPathPatterns("/**"); // 모든 경로에 인터셉터 적용
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(urlPrefix + "/**")
			.addResourceLocations("file:" + uploadPath + "/");
		log.info("정적 리소스 핸들러 등록: {} -> {}", urlPrefix, uploadPath);
	}
}

