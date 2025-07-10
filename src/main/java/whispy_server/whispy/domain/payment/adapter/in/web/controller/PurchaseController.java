package whispy_server.whispy.domain.payment.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.in.ValidatePurchaseUseCase;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final ValidatePurchaseUseCase validatePurchaseUseCase;

    @PostMapping("/validate")
    public ValidatePurchaseResponse validatePurchase(@RequestBody @Valid ValidatePurchaseRequest request) {
        return validatePurchaseUseCase.validateAndProcessPurchase(request);
    }
}
