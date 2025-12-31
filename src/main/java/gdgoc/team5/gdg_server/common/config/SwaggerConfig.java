package gdgoc.team5.gdg_server.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${spring.profiles.active:local}")
	private String activeProfile;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(
				new Info()
					.title("API Documentation")
					.version("1.0")
					.description("API documentation with JWT authentication"))
			// .servers(getServers())
			.components(
				new Components()
					.addSecuritySchemes(
						"accessToken",
						new SecurityScheme()
							.type(SecurityScheme.Type.HTTP)
							.scheme("bearer")
							.bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList("accessToken"));
	}

	// 활성화된 프로필에 따라 서버 목록 결정
	private List<Server> getServers() {
		List<Server> servers = new ArrayList<>();

		// 로컬 환경이면 localhost를 첫 번째로 설정
		if ("local".equals(activeProfile)) {
			servers.add(
				new Server().url("http://localhost:8080").description("Local Development Server"));
		}

		return servers;
	}
}