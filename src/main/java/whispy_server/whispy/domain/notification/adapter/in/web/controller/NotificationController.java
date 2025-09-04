package whispy_server.whispy.domain.notification.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.domain.notification.application.port.in.DeleteAllNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.in.DeleteNotificationUseCase;
import whispy_server.whispy.domain.notification.application.port.in.GetUnreadCountUseCase;
import whispy_server.whispy.domain.notification.application.port.in.MarkAllNotificationsAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.in.MarkNotificationAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.in.QueryMyNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.global.document.api.notification.NotificationApiDocument;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController implements NotificationApiDocument {

    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;
    private final SendToTopicUseCase sendToTopicUseCase;
    private final BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;
    private final QueryMyNotificationsUseCase queryMyNotificationsUseCase;
    private final GetUnreadCountUseCase getUnReadCountUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;
    private final DeleteNotificationUseCase deleteNotificationUseCase;
    private final DeleteAllNotificationsUseCase deleteAllNotificationsUseCase;

    @PostMapping("/send")
    public void sendNotification(@RequestBody @Valid NotificationSendRequest request) {
        sendToDeviceTokensUseCase.execute(request);
    }

    @PostMapping("/topic/send")
    public void sendToTopic(@RequestBody @Valid NotificationTopicSendRequest request) {
        sendToTopicUseCase.execute(request);
    }

    @PostMapping("/broadcast")
    public void broadcastToAllUsers(@RequestBody @Valid NotificationTopicSendRequest request) {
        broadCastToAllUsersUseCase.execute(request);
    }

    @GetMapping
    public List<NotificationResponse> getMyNotifications() {
        return queryMyNotificationsUseCase.execute();
    }

    @GetMapping("/unread/count")
    public UnreadCountResponse getUnreadCount() {
        return getUnReadCountUseCase.execute();
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable UUID notificationId) {
        markNotificationAsReadUseCase.execute(notificationId);
    }

    @PatchMapping("/read-all")
    public void markAllAsRead() {
        markAllNotificationsAsReadUseCase.execute();
    }

    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable UUID notificationId) {
        deleteNotificationUseCase.execute(notificationId);
    }

    @DeleteMapping
    public void deleteAllNotifications() {
        deleteAllNotificationsUseCase.execute();
    }

}