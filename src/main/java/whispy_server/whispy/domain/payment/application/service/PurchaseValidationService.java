package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.in.ValidatePurchaseUseCase;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;


@Service
@RequiredArgsConstructor
public class PurchaseValidationService implements ValidatePurchaseUseCase {

    private final GooglePlayApiPort googlePlayApiPort;
    private final PurchaseProcessingService purchaseProcessingService;

    @Override
    public ValidatePurchaseResponse validateAndProcessPurchase(ValidatePurchaseRequest request) {

            GooglePlaySubscriptionInfo subscriptionInfo = validateWithGooglePlay(request);
            return purchaseProcessingService.processValidatedPurchase(request, subscriptionInfo);
    }

    private GooglePlaySubscriptionInfo validateWithGooglePlay(ValidatePurchaseRequest request){

        GooglePlaySubscriptionInfo subscriptionInfo = googlePlayApiPort.getSubscriptionInfo(
                request.subscriptionId(), request.purchaseToken()
        );

        if(subscriptionInfo.paymentState() != 1){
            throw new IllegalArgumentException("dd");
        }
        return subscriptionInfo;
    }

}