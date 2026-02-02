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
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.payment.InvalidPaymentStateException;

/**
 * 구매 검증 서비스.
 *
 * Google Play 구매를 검증하고 처리하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class PurchaseValidationService implements ValidatePurchaseUseCase {

    private final GooglePlayApiPort googlePlayApiPort;
    private final PurchaseProcessingService purchaseProcessingService;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 구매를 검증하고 처리합니다.
     *
     * @param request 구매 검증 요청
     * @return 구매 검증 결과
     */
    @UserAction("구매 검증")
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

    /**
     * Google Play로 구매를 검증합니다.
     *
     * Google Play API를 호출하여 구독 정보를 가져온 후,
     * paymentState가 1(결제 완료)인지 검증합니다.
     *
     * paymentState:
     * - 0: 결제 대기중
     * - 1: 결제 완료 (유효)
     * - 2: 무료 체험
     * - 3: 결제 보류
     *
     * @param request 구매 검증 요청 (subscriptionId, purchaseToken)
     * @return Google Play 구독 정보
     * @throws InvalidPaymentStateException 결제 상태가 1(결제 완료)이 아닌 경우
     */
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