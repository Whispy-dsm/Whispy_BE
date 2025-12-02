package whispy_server.whispy.domain.payment.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.in.ValidatePurchaseUseCase;
import whispy_server.whispy.global.document.api.payment.PurchaseApiDocument;

/**
 * 구매 REST 컨트롤러.
 *
 * Google Play 구매 검증 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController implements PurchaseApiDocument {

    private final ValidatePurchaseUseCase validatePurchaseUseCase;

    /**
     * 구매를 검증합니다.
     *
     * @param request 구매 검증 요청
     * @return 구매 검증 결과
     */
    @PostMapping("/validate")
    public ValidatePurchaseResponse validatePurchase(@RequestBody @Valid ValidatePurchaseRequest request) {
        return validatePurchaseUseCase.validateAndProcessPurchase(request);
    }
}
