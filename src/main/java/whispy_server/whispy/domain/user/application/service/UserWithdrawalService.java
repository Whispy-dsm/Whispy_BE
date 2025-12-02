package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.application.port.out.DeleteFocusSessionPort;
import whispy_server.whispy.domain.history.application.port.out.DeleteListeningHistoryPort;
import whispy_server.whispy.domain.like.application.port.out.DeleteMusicLikePort;
import whispy_server.whispy.domain.meditationsession.application.port.out.DeleteMeditationSessionPort;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.sleepsession.application.port.out.DeleteSleepSessionPort;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.topic.application.port.out.DeleteTopicSubscriptionPort;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserWithdrawalUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserDeletePort;
import whispy_server.whispy.domain.user.model.User;

/**
 * 사용자 회원 탈퇴 서비스.
 * 사용자 계정과 관련된 모든 데이터를 삭제합니다.
 */
@Service
@RequiredArgsConstructor
public class UserWithdrawalService implements UserWithdrawalUseCase {

    private final UserDeletePort userDeletePort;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserFacadeUseCase userFacadeUseCase;

    // userId 기반
    private final DeleteFocusSessionPort deleteFocusSessionPort;
    private final DeleteSleepSessionPort deleteSleepSessionPort;
    private final DeleteMeditationSessionPort deleteMeditationSessionPort;
    private final DeleteMusicLikePort deleteMusicLikePort;
    private final DeleteListeningHistoryPort deleteListeningHistoryPort;
    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    // email 기반
    private final DeleteNotificationPort deleteNotificationPort;
    private final DeleteTopicSubscriptionPort deleteTopicSubscriptionPort;

    /**
     * 현재 인증된 사용자의 계정을 삭제합니다.
     * 세션 데이터, 좋아요, 히스토리, 알림 등 관련된 모든 데이터를 함께 삭제합니다.
     */
    @Override
    @Transactional
    public void withdrawal(){
        User currentUser = userFacadeUseCase.currentUser();
        Long userId = currentUser.id();
        String email = currentUser.email();

        // userId 기반
        deleteFocusSessionPort.deleteByUserId(userId);
        deleteSleepSessionPort.deleteByUserId(userId);
        deleteMeditationSessionPort.deleteByUserId(userId);
        deleteMusicLikePort.deleteByUserId(userId);
        deleteListeningHistoryPort.deleteByUserId(userId);
        deleteSoundSpaceMusicPort.deleteByUserId(userId);

        // email 기반
        deleteNotificationPort.deleteByEmail(email);
        deleteTopicSubscriptionPort.deleteByEmail(email);

        // User 삭제
        refreshTokenRepository.deleteById(email);
        userDeletePort.deleteById(userId);
    }
}
