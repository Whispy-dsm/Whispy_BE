package whispy_server.whispy.domain.fcm.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.fcm.application.port.in.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;
    private final SendToTopicUseCase sendToTopicUseCase;
    private final BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;
    private final SubscriptionTopicUseCase subscriptionTopicUseCase;
    private final UnSubscribeTopicUseCase unSubscribeTopicUseCase;
    private final QueryMyTopicSubscriptionsUseCase queryMyTopicSubscriptionsUseCase;
    private final UpdateFcmTokenUseCase updateFcmTokenUseCase;
    private final QueryMyNotificationsUseCase queryMyNotificationsUseCase;
    private final GetUnReadCountUseCase getUnReadCountUseCase;
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

    @PostMapping("/topic/subscribe")
    public void subscribeTopic(@RequestBody TopicSubscriptionRequest request) {
        subscriptionTopicUseCase.execute(request);
    }

    @PostMapping("/topic/unsubscribe")
    public void unsubscribeTopic(@RequestBody TopicSubscriptionRequest request) {
        unSubscribeTopicUseCase.execute(request);
    }

    @GetMapping("/subscriptions")
    public List<TopicSubscriptionResponse> getMySubscriptions() {
        return queryMyTopicSubscriptionsUseCase.execute();
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getMyNotifications() {
        return queryMyNotificationsUseCase.execute();
    }

    @GetMapping("/notifications/unread/count")
    public UnreadCountResponse getUnreadCount() {
        return getUnReadCountUseCase.execute();
    }

    @PatchMapping("/notifications/{notificationId}/read")
    public void markAsRead(@PathVariable UUID notificationId) {
        markNotificationAsReadUseCase.execute(notificationId);
    }

    @PatchMapping("/notifications/read-all")
    public void markAllAsRead() {
        markAllNotificationsAsReadUseCase.execute();
    }

    @PatchMapping("/update")
    public void updateFcmToken(@RequestBody UpdateFcmTokenRequest request) {
        updateFcmTokenUseCase.execute(request.fcmToken());
    }
}