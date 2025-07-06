package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;

public interface ValidatePurchaseUseCase {

    ValidatePurchaseResponse validateAndProcessPurchase(ValidatePurchaseRequest request);
}
