package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.application.port.in.QueryMyNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

/**
 * 내 알림 목록 조회 서비스.
 *
 * 현재 사용자의 알림 목록을 페이지네이션하여 조회하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryMyNotificationsService implements QueryMyNotificationsUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 내 알림 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 알림 목록 페이지
     */
    @Override
    public Page<NotificationResponse> execute(Pageable pageable){
        Page<Notification> notifications = queryNotificationPort.findByEmailOrderByCreatedAtDesc(
                userFacadeUseCase.currentUser().email(),
                pageable
        );

        return notifications.map(NotificationResponse::from);
    }
}
