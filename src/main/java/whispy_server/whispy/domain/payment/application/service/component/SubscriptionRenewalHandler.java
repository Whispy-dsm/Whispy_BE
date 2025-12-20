package whispy_server.whispy.domain.payment.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.SubscriptionNotificationRequest;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 구독 갱신 처리 컴포넌트.
 *
 * 구독 갱신을 트랜잭션 내에서 처리하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionRenewalHandler {

    private final QuerySubscriptionPort querySubscriptionPort;
    private final SubscriptionSavePort subscriptionSavePort;
    private final SubscriptionFactory subscriptionFactory;

    /**
     * 구독 갱신을 처리합니다.
     *
     * @param notification 구독 알림 정보
     * @param subscriptionInfo Google Play 구독 정보
     */
    @Transactional
    public void handleRenewal(SubscriptionNotificationRequest notification, GooglePlaySubscriptionInfo subscriptionInfo) {
        querySubscriptionPort.findByPurchaseToken(notification.purchaseToken())
                .filter(subscription ->
                                subscription.subscriptionState() == SubscriptionState.ACTIVE ||
                                subscription.subscriptionState() == SubscriptionState.GRACE_PERIOD ||
                                subscription.subscriptionState() == SubscriptionState.ON_HOLD
                )
                .ifPresent(subscription -> {
                    Subscription renewed = subscriptionFactory.renewedFrom(subscription,
                            LocalDateTime.ofEpochSecond(subscriptionInfo.expiryTimeMillis() / 1000, 0, ZoneOffset.UTC)
                    );
                    subscriptionSavePort.save(renewed);
                });
    }
}
