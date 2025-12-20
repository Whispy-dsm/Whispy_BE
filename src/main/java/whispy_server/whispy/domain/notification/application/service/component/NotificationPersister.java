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

    @Transactional
    public void save(Notification notification) {
        saveNotificationPort.save(notification);
    }
}