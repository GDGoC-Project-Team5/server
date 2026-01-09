package gdgoc.team5.gdg_server.common.controller.security;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import gdgoc.team5.gdg_server.common.controller.argresolver.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

	private static final String TOKEN_INFO_ATTRIBUTE = "tokenInfo";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {

		// 핸들러가 메서드인지 확인
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod)handler;

		// @AdminOnly 어노테이션이 있는지 확인
		AdminOnly adminOnly = handlerMethod.getMethodAnnotation(AdminOnly.class);
		if (adminOnly == null) {
			return true; // 어노테이션이 없으면 통과
		}

		// Request Attribute에서 TokenInfo 가져오기
		TokenInfo tokenInfo = (TokenInfo)request.getAttribute(TOKEN_INFO_ATTRIBUTE);

		// TokenInfo가 없거나 관리자가 아니면 403 에러
		if (tokenInfo == null || !Boolean.TRUE.equals(tokenInfo.isAdmin())) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"error\": \"관리자 권한이 필요합니다.\"}");
			return false;
		}

		return true;
	}
}
