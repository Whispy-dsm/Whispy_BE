package whispy_server.whispy.domain.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.DeveloperNotificationRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.SubscriptionNotificationRequest;
import whispy_server.whispy.domain.payment.application.service.component.SubscriptionRenewalHandler;
import whispy_server.whispy.domain.payment.application.service.component.SubscriptionUpdater;
import whispy_server.whispy.global.exception.domain.payment.InvalidSubscriptionNotificationException;
import whispy_server.whispy.global.exception.domain.payment.PurchaseNotificationProcessingFailedException;
import whispy_server.whispy.domain.payment.application.port.in.ProcessPurchaseNotificationUseCase;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.annotation.SystemAction;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 구매 알림 처리 서비스.
 *
 * Google Play Pub/Sub 메시지를 처리하고 구독 상태를 업데이트하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class PurchaseNotificationService implements ProcessPurchaseNotificationUseCase {

    private final GooglePlayApiPort googlePlayApiPort;
    private final ObjectMapper objectMapper;
    private final SubscriptionUpdater subscriptionUpdater;
    private final SubscriptionRenewalHandler subscriptionRenewalHandler;

    private static final int NOTIFICATION_TYPE_RECOVERED = 1;
    private static final int NOTIFICATION_TYPE_RENEWED = 2;
    private static final int NOTIFICATION_TYPE_CANCELED = 3;
    private static final int NOTIFICATION_TYPE_PURCHASED = 4;
    private static final int NOTIFICATION_TYPE_ON_HOLD = 5;
    private static final int NOTIFICATION_TYPE_IN_GRACE_PERIOD = 6;
    private static final int NOTIFICATION_TYPE_PAUSED = 10;
    private static final int NOTIFICATION_TYPE_REVOKED = 12;
    private static final int NOTIFICATION_TYPE_EXPIRED = 13;

    /**
     * Google Play Pub/Sub 메시지를 처리합니다.
     *
     * @param pubSubMessage 처리할 Pub/Sub 메시지
     */
    @SystemAction("Pub/Sub 구매 알림 처리")
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
            throw new PurchaseNotificationProcessingFailedException(e);
        }
    }

    /**
     * 구독 알림을 처리합니다.
     *
     * Google Play Pub/Sub 알림 타입별 처리:
     * - RECOVERED(1): 결제 문제 해결됨 → ACTIVE
     * - RENEWED(2): 구독 갱신됨 → 별도 처리 (handleSubscriptionRenewed)
     * - CANCELED(3): 구독 취소됨 → CANCELED
     * - PURCHASED(4): 신규 구매 → 무처리 (validateAndProcessPurchase에서 처리)
     * - ON_HOLD(5): 결제 보류 중 → ON_HOLD
     * - IN_GRACE_PERIOD(6): 유예 기간 → GRACE_PERIOD
     * - PAUSED(10): 일시 중지 → PAUSED
     * - REVOKED(12): 구독 취소 및 환불 → REVOKED
     * - EXPIRED(13): 구독 만료 → EXPIRED
     *
     * @param notification 구독 알림 정보 (notificationType, purchaseToken, subscriptionId)
     * @throws InvalidSubscriptionNotificationException 알 수 없는 알림 타입인 경우
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
     * 프로세스:
     * 1. Google Play API에서 최신 구독 정보 조회 (트랜잭션 밖)
     * 2. DB에 구독 갱신 정보 업데이트 (트랜잭션 안)
     *
     * 이유:
     * - 외부 API 호출은 트랜잭션 밖에서 실행하여 DB 커넥션 점유 최소화
     * - 조회한 정보로 DB 업데이트는 트랜잭션 안에서 원자성 보장
     *
     * @param notification 구독 알림 정보 (purchaseToken, subscriptionId)
     */
    private void handleSubscriptionRenewed(SubscriptionNotificationRequest notification) {
        // 트랜잭션 밖에서 외부 API 호출
        GooglePlaySubscriptionInfo subscriptionInfo = googlePlayApiPort.getSubscriptionInfo(
                notification.subscriptionId(),
                notification.purchaseToken()
        );

        // 트랜잭션 안에서 DB 저장
        subscriptionRenewalHandler.handleRenewal(notification, subscriptionInfo);
    }
}
