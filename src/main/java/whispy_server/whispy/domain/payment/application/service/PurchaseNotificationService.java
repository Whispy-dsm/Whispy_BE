package whispy_server.whispy.domain.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.DeveloperNotificationRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.SubscriptionNotificationRequest;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionUpdater;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.global.exception.domain.payment.InvalidSubscriptionNotificationException;
import whispy_server.whispy.global.exception.domain.payment.PurchaseNotificationProcessingFailedException;
import whispy_server.whispy.domain.payment.application.port.in.ProcessPurchaseNotificationUseCase;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

/**
 * 구매 알림 처리 서비스.
 *
 * Google Play Pub/Sub 메시지를 처리하고 구독 상태를 업데이트하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class PurchaseNotificationService implements ProcessPurchaseNotificationUseCase {

    private final SubscriptionSavePort subscriptionSavePort;
    private final QuerySubscriptionPort querySubscriptionPort;
    private final GooglePlayApiPort googlePlayApiPort;
    private final ObjectMapper objectMapper;
    private final SubscriptionFactory subscriptionFactory;
    private final SubscriptionUpdater subscriptionUpdater;

    /**
     * Google Play Pub/Sub 메시지를 처리합니다.
     *
     * @param pubSubMessage 처리할 Pub/Sub 메시지
     */
    @Transactional
    @Override
    public void processPubSubMessage(PubSubMessageRequest pubSubMessage) {
        try {
            String data = pubSubMessage.message().data();
            byte[] decodedBytes = Base64.getDecoder().decode(data);
            String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);

            DeveloperNotificationRequest notification = objectMapper.readValue(jsonString, DeveloperNotificationRequest.class);

            if (notification.subscriptionNotification() != null) {
                handleSubscriptionNotification(notification.subscriptionNotification());
            }

        } catch (Exception e) {
            throw PurchaseNotificationProcessingFailedException.EXCEPTION;
        }
    }

    /**
     * 구독 알림을 처리합니다.
     *
     * @param notification 구독 알림 정보
     */
    private void handleSubscriptionNotification(SubscriptionNotificationRequest notification) {
        switch (notification.notificationType()) {
            case NOTIFICATION_TYPE_RECOVERED -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.ACTIVE);
            case NOTIFICATION_TYPE_RENEWED -> handleSubscriptionRenewed(notification);
            case NOTIFICATION_TYPE_CANCELED -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.CANCELED);
            case NOTIFICATION_TYPE_PURCHASED -> { }
            case NOTIFICATION_TYPE_ON_HOLD -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.ON_HOLD);
            case NOTIFICATION_TYPE_IN_GRACE_PERIOD -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.GRACE_PERIOD);
            case NOTIFICATION_TYPE_PAUSED -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.PAUSED);
            case NOTIFICATION_TYPE_REVOKED -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.REVOKED);
            case NOTIFICATION_TYPE_EXPIRED -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.EXPIRED);
            default -> throw InvalidSubscriptionNotificationException.EXCEPTION;
        }
    }

    /**
     * 구독 갱신을 처리합니다.
     *
     * @param notification 구독 알림 정보
     */
    private void handleSubscriptionRenewed(SubscriptionNotificationRequest notification) {
        GooglePlaySubscriptionInfo subscriptionInfo = googlePlayApiPort.getSubscriptionInfo(
                notification.subscriptionId(),
                notification.purchaseToken()
        );

        querySubscriptionPort.findByPurchaseToken(notification.purchaseToken())
                .filter(subscription -> {
                    return subscription.subscriptionState() == SubscriptionState.ACTIVE ||
                            subscription.subscriptionState() == SubscriptionState.GRACE_PERIOD ||
                            subscription.subscriptionState() == SubscriptionState.ON_HOLD;
                })
                .ifPresent(subscription -> {
           Subscription renewed = subscriptionFactory.renewedFrom(subscription,
                   LocalDateTime.ofEpochSecond(subscriptionInfo.expiryTimeMillis() / 1000, 0, ZoneOffset.UTC)
           );
           subscriptionSavePort.save(renewed);
        });

    }

    private static final int NOTIFICATION_TYPE_RECOVERED = 1;
    private static final int NOTIFICATION_TYPE_RENEWED = 2;
    private static final int NOTIFICATION_TYPE_CANCELED = 3;
    private static final int NOTIFICATION_TYPE_PURCHASED = 4;
    private static final int NOTIFICATION_TYPE_ON_HOLD = 5;
    private static final int NOTIFICATION_TYPE_IN_GRACE_PERIOD = 6;
    private static final int NOTIFICATION_TYPE_PAUSED = 10;
    private static final int NOTIFICATION_TYPE_REVOKED = 12;
    private static final int NOTIFICATION_TYPE_EXPIRED = 13;
}
