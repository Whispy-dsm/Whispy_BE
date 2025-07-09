package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.in.ValidatePurchaseUseCase;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.payment.InvalidPaymentStateException;


@Service
@RequiredArgsConstructor
public class PurchaseValidationService implements ValidatePurchaseUseCase {

    private final GooglePlayApiPort googlePlayApiPort;
    private final PurchaseProcessingService purchaseProcessingService;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public ValidatePurchaseResponse validateAndProcessPurchase(ValidatePurchaseRequest request) {

        User currentUser = userFacadeUseCase.currentUser();

        GooglePlaySubscriptionInfo subscriptionInfo = validateWithGooglePlay(request);
        return purchaseProcessingService.processValidatedPurchase(
                currentUser.email(),
                request.purchaseToken(),
                request.subscriptionId(),
                subscriptionInfo
        );
    }

    private GooglePlaySubscriptionInfo validateWithGooglePlay(ValidatePurchaseRequest request){

        GooglePlaySubscriptionInfo subscriptionInfo = googlePlayApiPort.getSubscriptionInfo(
                request.subscriptionId(), request.purchaseToken()
        );

        if(subscriptionInfo.paymentState() != 1){
            throw InvalidPaymentStateException.EXCEPTION;
        }
        return subscriptionInfo;
    }
}