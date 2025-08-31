package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.auth.adapter.out.persistence.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.application.port.in.UpdateFcmTokenUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.facade.UserFacade;
import whispy_server.whispy.domain.user.model.User;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateFcmTokenService implements UpdateFcmTokenUseCase {

    private final UserFacade userFacade;
    private final UserSavePort userSavePort;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmSendPort fcmSendPort;

    @Override
    public void execute(String fcmToken) {
        User currentUser = userFacade.currentUser();

        if (fcmToken != null && !fcmToken.equals(currentUser.fcmToken())) {
            // 기존 토큰으로 로그아웃 알림 전송
            if (currentUser.fcmToken() != null && !currentUser.fcmToken().trim().isEmpty()) {
                sendLogoutNotification(currentUser.fcmToken());
            }
            
            // 기존 RefreshToken 강제 삭제 (기존 세션 무효화)
            refreshTokenRepository.deleteById(currentUser.email());
            
            User updatedUser = currentUser.updateFcmToken(fcmToken);
            userSavePort.save(updatedUser);

            initializeTopicsUseCase.execute(currentUser.email(), fcmToken);
        }
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
            // 알림 전송 실패해도 FCM 토큰 업데이트는 계속 진행
        }
    }
}
