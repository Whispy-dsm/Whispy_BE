package whispy_server.whispy.domain.payment.application.service.domain;

import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.exception.domain.payment.UnknownProductIdException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class SubscriptionFactory {

    public Subscription createNewSubscription(String email, String purchaseToken, String googlePlayProductId, GooglePlaySubscriptionInfo info) {
        return new Subscription(
                null,
                email,
                purchaseToken,
                getProductTypeFromGooglePlayId(googlePlayProductId),
                toLocalDateTime(info.startTimeMillis()),
                SubscriptionState.ACTIVE,
                true,
                toLocalDateTime(info.expiryTimeMillis())
        );
    }

    public Subscription renewedFrom(Subscription subscription, LocalDateTime newExpiryTime){
        return new Subscription(
                subscription.id(),
                subscription.email(),
                subscription.purchaseToken(),
                subscription.productType(),
                subscription.purchaseTime(),
                SubscriptionState.ACTIVE,
                subscription.autoRenewing(),
                newExpiryTime
        );
    }

    public Subscription withState(Subscription subscription, SubscriptionState newState) {
        return new Subscription(
                subscription.id(),
                subscription.email(),
                subscription.purchaseToken(),
                subscription.productType(),
                subscription.purchaseTime(),
                newState,
                subscription.autoRenewing(),
                subscription.expiryTime()
        );
    }

        private ProductType getProductTypeFromGooglePlayId (String googlePlayId){
            return switch (googlePlayId) {
                case "monthly_basic" -> ProductType.MONTHLY;
                default -> throw UnknownProductIdException.EXCEPTION;
            };
        }

        private LocalDateTime toLocalDateTime(long epochMillis){
        return LocalDateTime.ofEpochSecond(epochMillis/ 1000, 0, ZoneOffset.UTC);
        }

    }

