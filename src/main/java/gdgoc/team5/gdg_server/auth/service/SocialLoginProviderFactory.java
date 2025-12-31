package gdgoc.team5.gdg_server.auth.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import gdgoc.team5.gdg_server.auth.domain.MemberType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialLoginProviderFactory {

	private final List<SocialLoginProvider> socialLoginProviders;
	private Map<MemberType, SocialLoginProvider> providerMap;

	@PostConstruct
	public void init() {
		this.providerMap =
			socialLoginProviders.stream()
				.collect(
					Collectors.toMap(SocialLoginProvider::getSupportedMemberType, Function.identity()));
	}

	public SocialLoginProvider getProvider(MemberType memberType) {
		SocialLoginProvider provider = providerMap.get(memberType);
		if (provider == null) {
			throw new RuntimeException("존재하지 않는 로그인 타입입니다: " + memberType);
		}
		return provider;
	}
}
