package whispy_server.whispy.domain.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionUpdater;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.global.feign.google.dto.*;
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

@Service
@RequiredArgsConstructor
public class PurchaseNotificationService implements ProcessPurchaseNotificationUseCase {

    private final SubscriptionSavePort subscriptionSavePort;
    private final QuerySubscriptionPort querySubscriptionPort;
    private final GooglePlayApiPort googlePlayApiPort;
    private final ObjectMapper objectMapper;
    private final SubscriptionFactory subscriptionFactory;
    private final SubscriptionUpdater subscriptionUpdater;

    @Transactional
    @Override
    public void processPubSubMessage(PubSubMessage pubSubMessage) {
        try {
            String data = pubSubMessage.message().data();
            byte[] decodedData = Base64.getDecoder().decode(data);
            String jsonString = new String(decodedData, StandardCharsets.UTF_8);

            DeveloperNotification notification = objectMapper.readValue(jsonString, DeveloperNotification.class);

            if (notification.subscriptionNotification() != null) {
                handleSubscriptionNotification(notification.subscriptionNotification());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process notification", e);
        }
    }

    private void handleSubscriptionNotification(SubscriptionNotification notification) {
        switch (notification.notificationType()) {
            case 1 -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.ACTIVE);
            case 2 -> handleSubscriptionRenewed(notification);
            case 3 -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.CANCELED);
            case 4 -> handleSubscriptionPurchased(notification);
            case 5 -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.ON_HOLD);
            case 6 -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.GRACE_PERIOD);
            case 12 -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.REVOKED);
            case 13 -> subscriptionUpdater.updateState(notification.purchaseToken(), SubscriptionState.EXPIRED);
            default -> throw new IllegalArgumentException("dd");
        }
    }

    private void handleSubscriptionPurchased(SubscriptionNotification notification) {
        GooglePlaySubscriptionInfo subscriptionInfo = googlePlayApiPort.getSubscriptionInfo(
                notification.subscriptionId(),
                notification.purchaseToken()
        );

        String email = subscriptionInfo.obfuscatedExternalAccountId();

        if (!StringUtils.hasText(subscriptionInfo.linkedPurchaseToken())) {
            Subscription newSubscription = subscriptionFactory.createNewSubscription(
                    email,
                    notification.purchaseToken(),
                    notification.subscriptionId(),
                    subscriptionInfo
            );
            subscriptionSavePort.save(newSubscription);

        }else {

            querySubscriptionPort.findByPurchaseToken(subscriptionInfo.linkedPurchaseToken())
                    .filter(Subscription::isActive)
                    .ifPresent(old -> {
                        Subscription upgraded = subscriptionFactory.upgradeFrom(old);
                        subscriptionSavePort.save(upgraded);
                    });

            Subscription newSubscription = subscriptionFactory.createNewSubscription(email,
                    notification.purchaseToken(),
                    notification.subscriptionId(),
                    subscriptionInfo
            );
            subscriptionSavePort.save(newSubscription);
        }
        googlePlayApiPort.acknowledgeSubscription(notification.subscriptionId(), notification.purchaseToken());
    }

    private void handleSubscriptionRenewed(SubscriptionNotification notification) {
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
}
