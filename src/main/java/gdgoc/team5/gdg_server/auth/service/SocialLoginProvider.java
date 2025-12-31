package gdgoc.team5.gdg_server.auth.service;

import gdgoc.team5.gdg_server.auth.controller.request.SocialLoginRequestDto;
import gdgoc.team5.gdg_server.auth.domain.Member;
import gdgoc.team5.gdg_server.auth.domain.MemberType;
import jakarta.servlet.http.HttpServletRequest;

public interface SocialLoginProvider {
	MemberType getSupportedMemberType();

	Member getMemberInfo(SocialLoginRequestDto dto, HttpServletRequest servLetRequest);
}