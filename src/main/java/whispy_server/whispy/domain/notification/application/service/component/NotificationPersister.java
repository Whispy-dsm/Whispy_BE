    package whispy_server.whispy.domain.notification.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;

/**
 * 알림 영속화 컴포넌트 (NotificationPersister).
 * * 알림 데이터를 트랜잭션 내에서 DB에 저장하는 역할을 전담합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationPersister {

    private final SaveNotificationPort saveNotificationPort;

    /**
     * 알림을 데이터베이스에 저장합니다.
     *
     * 트랜잭션 내에서 실행되며, FCM 전송 실패 시에도 알림 기록은 DB에 보관됩니다.
     * 이를 통해 사용자는 앱 내에서 놓친 알림을 확인할 수 있습니다.
     *
     * @param notification 저장할 알림 도메인 객체
     */
    @Transactional
    public void save(Notification notification) {
        saveNotificationPort.save(notification);
    }
}