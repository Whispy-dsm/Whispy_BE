package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.InitializeTopicsRequest;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.domain.user.application.port.in.UpdateFcmTokenUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.facade.UserFacade;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.List;

/**
 * FCM 토큰 업데이트 서비스.
 * 푸시 알림을 위한 Firebase Cloud Messaging 토큰을 업데이트합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateFcmTokenService implements UpdateFcmTokenUseCase {

    private final UserFacade userFacade;
    private final UserSavePort userSavePort;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * FCM 토큰을 업데이트합니다.
     * 토큰 변경 시 이전 기기에 로그아웃 알림을 보내고 기존 세션을 무효화합니다.
     *
     * @param request FCM 토큰 업데이트 요청
     */
    @Override
    @UserAction("FCM 토큰 업데이트")
    public void execute(UpdateFcmTokenRequest request) {
        User currentUser = userFacade.currentUser();
        String fcmToken = request.fcmToken();

        if (fcmToken != null && !fcmToken.equals(currentUser.fcmToken())) {
            // 기존 토큰으로 로그아웃 알림 전송
            if (currentUser.fcmToken() != null && !currentUser.fcmToken().trim().isEmpty()) {
                sendLogoutNotification(currentUser.fcmToken(), currentUser.email());
            }

            // 기존 RefreshToken 강제 삭제 (기존 세션 무효화)
            refreshTokenRepository.deleteById(currentUser.id());

            User updatedUser = currentUser.updateFcmToken(fcmToken);
            userSavePort.save(updatedUser);

            initializeTopicsUseCase.execute(
                    new InitializeTopicsRequest(currentUser.email(), fcmToken, false)
            );
        }
    }

    /**
     * 이전 기기에 로그아웃 알림을 전송합니다.
     *
     * 새로운 기기에서 FCM 토큰을 업데이트할 때, 기존 기기에 푸시 알림을 보내
     * 다른 기기에서 로그인하여 자동 로그아웃되었음을 알립니다.
     *
     * 알림 전송 실패 시에도 예외를 던지지 않고 토큰 업데이트는 계속 진행합니다.
     *
     * @param oldToken 이전 기기의 FCM 토큰
     * @param email    사용자 이메일
     */
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
            // 알림 전송 실패해도 FCM 토큰 업데이트는 계속 진행
        }
    }
}
