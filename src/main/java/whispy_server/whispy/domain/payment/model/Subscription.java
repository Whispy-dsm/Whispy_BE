package whispy_server.whispy.domain.payment.model;

import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

@Aggregate
public record Subscription(
        Long id,
        String email,
        String purchaseToken,
        ProductType productType,
        LocalDateTime purchaseTime,
        SubscriptionState subscriptionState,
        Boolean autoRenewing,
        LocalDateTime expiryTime
) {
    
    public boolean isActive() {
        return switch (subscriptionState) {
            case ACTIVE, GRACE_PERIOD -> LocalDateTime.now().isBefore(expiryTime);
            case CANCELED -> LocalDateTime.now().isBefore(expiryTime);
            default -> false;
        };
    }
}
