package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
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
import whispy_server.whispy.global.annotation.UserAction;

import java.util.List;
import java.util.Map;

/**
 * 카카오 OAuth 로그인 서비스.
 * 카카오 액세스 토큰으로 사용자 정보를 가져와 인증하고 JWT 토큰을 발급합니다.
 */
@Service
@RequiredArgsConstructor
public class KakaoOauthService implements KakaoOauthUseCase {

    private final KakaoUserInfoAdapter kakaoUserInfoAdapter;
    private final OauthUserUseCase oauthUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final UserSavePort userSavePort;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;

    /**
     * 카카오 액세스 토큰으로 로그인하고 JWT 토큰을 발급합니다.
     * 신규 사용자의 경우 자동으로 회원가입이 진행됩니다.
     *
     * @param accessToken 카카오에서 발급받은 액세스 토큰
     * @param fcmToken Firebase Cloud Messaging 토큰 (선택)
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    @Override
    @Transactional
    @UserAction("카카오 OAuth 로그인")
    public TokenResponse loginWithKakao(String accessToken, String fcmToken) {
        Map<String, Object> userAttribute = kakaoUserInfoAdapter.fetchUserInfo(accessToken);
        OauthUserInfo oauthUserInfo = new KakaoOauthUserInfoParser().parse(userAttribute);
        User user = oauthUserUseCase.findOrCreateOauthUser(oauthUserInfo, "kakao");

        if (fcmToken != null && !fcmToken.equals(user.fcmToken())) {

            if (user.fcmToken() != null && !user.fcmToken().trim().isEmpty()) {
                sendLogoutNotification(user.fcmToken(), user.email());
            }

            User updatedUser = user.updateFcmToken(fcmToken);
            userSavePort.save(updatedUser);

            initializeTopicsUseCase.execute(user.email(), fcmToken, false);
            user = updatedUser;
        }

        // (기존 세션 무효화)
        refreshTokenRepository.deleteById(user.email());

        return jwtTokenProvider.generateToken(user.email(), user.role().name());
    }

    /** 이전 기기에 로그아웃 알림을 전송합니다 */
    private void sendLogoutNotification(String oldToken, String email) {
        try {
            NotificationSendRequest request = new NotificationSendRequest(
                    email,
                    List.of(oldToken),
                    NotificationTopic.SYSTEM_ANNOUNCEMENT,
                    "로그아웃",
                    "다른 기기에서 로그인하여 자동 로그아웃 되었습니다.",
                    null
            );

            sendToDeviceTokensUseCase.execute(request);
        } catch (Exception e) {
            // 알림 전송 실패해도 로그인은 계속 진행
        }
    }
}