package whispy_server.whispy.domain.notification.application.port.out;

/**
 * 알림 아웃바운드 포트.
 *
 * 알림 조회, 저장, 삭제 포트를 통합한 아웃바운드 포트입니다.
 */
public interface NotificationPort extends QueryNotificationPort, SaveNotificationPort, DeleteNotificationPort {
}
