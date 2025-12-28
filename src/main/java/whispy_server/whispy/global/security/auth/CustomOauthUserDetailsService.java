package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import whispy_server.whispy.global.oauth.parser.factory.OauthUserInfoParserFactory;

/**
 * OAuth2 로그인 시 사용자 정보를 조회/생성하는 서비스.
 */
@RequiredArgsConstructor
@Service
public class CustomOauthUserDetailsService extends DefaultOAuth2UserService {

    private final OauthUserUseCase oauthUserUseCase;

    /**
     * OAuth2 정보를 기반으로 Whispy 사용자 계정을 조회하거나 생성한다.
     */
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OauthUserInfo oauthUserInfo = OauthUserInfoParserFactory.getParser(provider).parse(oAuth2User.getAttributes());
        User user = oauthUserUseCase.findOrCreateOauthUser(oauthUserInfo, provider);

        return new AuthDetails(user.id(), user.role().name(), oAuth2User.getAttributes());
    }

}
