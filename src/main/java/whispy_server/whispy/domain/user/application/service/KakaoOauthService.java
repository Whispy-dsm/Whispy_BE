package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.auth.adapter.out.persistence.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.KakaoOauthUseCase;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.adapter.out.external.KakaoUserInfoAdapter;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import whispy_server.whispy.global.oauth.parser.KakaoOauthUserInfoParser;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOauthService implements KakaoOauthUseCase {

    private final KakaoUserInfoAdapter kakaoUserInfoAdapter;
    private final OauthUserUseCase oauthUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final UserSavePort userSavePort;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmSendPort fcmSendPort;

    @Override
    @Transactional
    public TokenResponse loginWithKakao(String accessToken, String fcmToken) {
        Map<String, Object> userAttribute = kakaoUserInfoAdapter.fetchUserInfo(accessToken);
        OauthUserInfo oauthUserInfo = new KakaoOauthUserInfoParser().parse(userAttribute);
        User user = oauthUserUseCase.findOrCreateOauthUser(oauthUserInfo, "kakao");

        if (fcmToken != null && !fcmToken.equals(user.fcmToken())) {

            if (user.fcmToken() != null && !user.fcmToken().trim().isEmpty()) {
                sendLogoutNotification(user.fcmToken());
            }

            User updatedUser = user.updateFcmToken(fcmToken);
            userSavePort.save(updatedUser);

            initializeTopicsUseCase.execute(user.email(), fcmToken);
            user = updatedUser;
        }

        // (기존 세션 무효화)
        refreshTokenRepository.deleteById(user.email());

        return jwtTokenProvider.generateToken(user.email(), user.role().name());
    }

    private void sendLogoutNotification(String oldToken) {
        try {
            fcmSendPort.sendMulticast(
                    List.of(oldToken),
                    "로그아웃",
                    "다른 기기에서 로그인하여 자동 로그아웃되었습니다.",
                    Map.of("type", "forced_logout")
            );
        } catch (Exception e) {
            // 알림 전송 실패해도 로그인은 계속 진행
        }
    }
}