package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 알림 읽음 처리 유스케이스.
 *
 * 특정 알림을 읽음 상태로 변경하는 인바운드 포트입니다.
 */
@UseCase
public interface MarkNotificationAsReadUseCase {
    /**
     * 특정 알림을 읽음 상태로 변경합니다.
     *
     * @param notificationId 읽음 처리할 알림 ID
     */
    void execute(Long notificationId);
}
