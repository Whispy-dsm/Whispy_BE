package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 알림 삭제 유스케이스.
 *
 * 특정 알림을 삭제하는 인바운드 포트입니다.
 */
@UseCase
public interface DeleteNotificationUseCase {
    /**
     * 특정 알림을 삭제합니다.
     *
     * @param notificationId 삭제할 알림 ID
     */
    void execute(Long notificationId);
}