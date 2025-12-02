package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 모든 알림 삭제 유스케이스.
 *
 * 사용자의 모든 알림을 삭제하는 인바운드 포트입니다.
 */
@UseCase
public interface DeleteAllNotificationsUseCase {
    /**
     * 사용자의 모든 알림을 삭제합니다.
     */
    void execute();
}
