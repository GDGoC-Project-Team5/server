package gdgoc.team5.gdg_server.common.controller.argresolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenInfoArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String TOKEN_INFO_ATTRIBUTE = "tokenInfo";

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthenticatedToken.class) &&
			parameter.getParameterType().equals(TokenInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		return webRequest.getAttribute(TOKEN_INFO_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}
}