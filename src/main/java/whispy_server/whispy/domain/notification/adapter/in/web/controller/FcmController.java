package whispy_server.whispy.domain.notification.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.domain.notification.application.port.in.GetUnreadCountUseCase;
import whispy_server.whispy.domain.notification.application.port.in.MarkAllNotificationsAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.in.MarkNotificationAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.in.QueryMyNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class FcmController {

    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;
    private final SendToTopicUseCase sendToTopicUseCase;
    private final BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;
    private final QueryMyNotificationsUseCase queryMyNotificationsUseCase;
    private final GetUnreadCountUseCase getUnReadCountUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;

    @PostMapping("/send")
    public void sendNotification(@RequestBody FcmSendRequest request) {
        sendToDeviceTokensUseCase.execute(request);
    }

    @PostMapping("/topic/send")
    public void sendToTopic(@RequestBody FcmSendRequest request) {
        sendToTopicUseCase.execute(request);
    }

    @PostMapping("/broadcast")
    public void broadcastToAllUsers(@RequestBody FcmSendRequest request) {
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

}