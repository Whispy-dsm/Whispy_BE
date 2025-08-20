package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.fcm.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.KakaoOauthUseCase;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.adapter.out.external.KakaoUserInfoAdapter;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import whispy_server.whispy.global.oauth.parser.KakaoOauthUserInfoParser;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOauthService implements KakaoOauthUseCase {

    private final KakaoUserInfoAdapter kakaoUserInfoAdapter;
    private final OauthUserUseCase oauthUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final UserSavePort userSavePort;

    @Override
    @Transactional
    public TokenResponse loginWithKakao(String accessToken, String fcmToken){
        Map<String, Object> userAttribute = kakaoUserInfoAdapter.fetchUserInfo(accessToken) ;
        OauthUserInfo oauthUserInfo = new KakaoOauthUserInfoParser().parse(userAttribute);
        User user = oauthUserUseCase.findOrCreateOauthUser(oauthUserInfo, "kakao");

        if (fcmToken != null && !fcmToken.equals(user.fcmToken())) {
            User updatedUser = user.updateFcmToken(fcmToken);
            userSavePort.save(updatedUser);

            initializeTopicsUseCase.execute(user.email(), fcmToken);
        }

        return jwtTokenProvider.generateToken(user.email(), user.role().name());
    }
}
