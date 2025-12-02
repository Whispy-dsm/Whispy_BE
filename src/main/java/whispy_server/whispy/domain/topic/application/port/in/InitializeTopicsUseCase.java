package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 토픽 초기화 유스케이스.
 *
 * 신규 사용자의 토픽을 초기화하거나 기존 사용자의 FCM 토큰을 재등록합니다.
 */
@UseCase
public interface InitializeTopicsUseCase {
    /**
     * 사용자의 토픽을 초기화합니다.
     *
     * @param email 사용자 이메일
     * @param fcmToken FCM 토큰
     * @param isEventAgreed 이벤트 수신 동의 여부
     */
    void execute(String email, String fcmToken, boolean isEventAgreed);
}
