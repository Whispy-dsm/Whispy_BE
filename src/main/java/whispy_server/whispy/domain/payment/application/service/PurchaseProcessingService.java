package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PurchaseProcessingService {

    private final SubscriptionSavePort subscriptionSavePort;
    private final QuerySubscriptionPort querySubscriptionPort;
    private final SubscriptionFactory subscriptionFactory;
    private final GooglePlayApiPort googlePlayApiPort;

    @Transactional
    public ValidatePurchaseResponse processValidatedPurchase(
            String email,
            String purchaseToken,
            String subscriptionId,
            GooglePlaySubscriptionInfo subscriptionInfo) {

        Optional<Subscription> existingSubscription = querySubscriptionPort.findByPurchaseToken(purchaseToken);
        if (existingSubscription.isPresent()) {
            return new ValidatePurchaseResponse(true, "Purchase already processed");
        }

        Subscription subscription = subscriptionFactory.createNewSubscription(
                email,
                purchaseToken,
                subscriptionId,
                subscriptionInfo
        );

        subscriptionSavePort.save(subscription);

        try {
            googlePlayApiPort.acknowledgeSubscription(subscriptionId, purchaseToken);
        } catch (Exception e) {
            // acknowledge 실패해도 구독은 이미 저장됨
        }

        return new ValidatePurchaseResponse(true, "Purchase validated successfully");
    }
}
