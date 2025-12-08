package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;

/**
 * 구매 검증 유스케이스.
 *
 * Google Play 구매를 검증하고 처리하는 인바운드 포트입니다.
 */
public interface ValidatePurchaseUseCase {

    /**
     * 구매를 검증하고 처리합니다.
     *
     * @param request 구매 검증 요청
     * @return 구매 검증 결과
     */
    ValidatePurchaseResponse validateAndProcessPurchase(ValidatePurchaseRequest request);
}
