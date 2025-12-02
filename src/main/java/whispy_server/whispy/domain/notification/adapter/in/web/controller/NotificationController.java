package whispy_server.whispy.domain.notification.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.notification.application.port.in.DeleteAllNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.in.DeleteNotificationUseCase;
import whispy_server.whispy.domain.notification.application.port.in.GetUnreadCountUseCase;
import whispy_server.whispy.domain.notification.application.port.in.MarkAllNotificationsAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.in.MarkNotificationAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.in.QueryMyNotificationsUseCase;
import whispy_server.whispy.global.document.api.notification.NotificationApiDocument;

/**
 * 알림 REST 컨트롤러.
 *
 * 사용자 알림 조회, 읽음 처리, 삭제 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController implements NotificationApiDocument {

    private final QueryMyNotificationsUseCase queryMyNotificationsUseCase;
    private final GetUnreadCountUseCase getUnReadCountUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;
    private final DeleteNotificationUseCase deleteNotificationUseCase;
    private final DeleteAllNotificationsUseCase deleteAllNotificationsUseCase;

    /**
     * 내 알림 목록을 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 알림 목록 페이지
     */
    @GetMapping
    public Page<NotificationResponse> getMyNotifications(Pageable pageable) {
        return queryMyNotificationsUseCase.execute(pageable);
    }

    @GetMapping("/unread/count")
    public UnreadCountResponse getUnreadCount() {
        return getUnReadCountUseCase.execute();
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId) {
        markNotificationAsReadUseCase.execute(notificationId);
    }

    @PatchMapping("/read-all")
    public void markAllAsRead() {
        markAllNotificationsAsReadUseCase.execute();
    }

    @DeleteMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(@PathVariable Long notificationId) {
        deleteNotificationUseCase.execute(notificationId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllNotifications() {
        deleteAllNotificationsUseCase.execute();
    }

}
