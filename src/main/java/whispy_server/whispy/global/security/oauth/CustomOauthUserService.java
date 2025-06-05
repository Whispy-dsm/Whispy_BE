package whispy_server.whispy.global.security.oauth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import whispy_server.whispy.global.oauth.parser.factory.OauthUserInfoParserFactory;
import whispy_server.whispy.global.security.auth.AuthDetails;

@RequiredArgsConstructor
@Service
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final OauthUserUseCase oauthUserUseCase;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OauthUserInfo oauthUserInfo = OauthUserInfoParserFactory.getParser(provider).parse(oAuth2User.getAttributes());
        User user = oauthUserUseCase.findOrCreateOauthUser(oauthUserInfo, provider);

        return new AuthDetails(user.email(), user.role().name(), oAuth2User.getAttributes());
    }

}
