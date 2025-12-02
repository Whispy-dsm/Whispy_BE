package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 모든 알림 읽음 처리 유스케이스.
 *
 * 사용자의 모든 알림을 읽음 상태로 변경하는 인바운드 포트입니다.
 */
@UseCase
public interface MarkAllNotificationsAsReadUseCase {
    /**
     * 모든 알림을 읽음 상태로 변경합니다.
     */
    void execute();
}
