package whispy_server.whispy.domain.notification.application.port.out;

import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;

/**
 * 알림 저장 아웃바운드 포트.
 *
 * 알림을 저장하는 영속성 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface SaveNotificationPort {
    /**
     * 알림을 저장합니다.
     *
     * @param notification 저장할 알림
     */
    void save(Notification notification);

    /**
     * 여러 알림을 일괄 저장합니다.
     *
     * @param notifications 저장할 알림 목록
     */
    void saveAll(List<Notification> notifications);
}
