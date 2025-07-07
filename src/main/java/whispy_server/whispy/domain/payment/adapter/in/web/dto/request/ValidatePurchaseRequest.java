package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ValidatePurchaseRequest(
        @Email
        String email,
        @NotBlank
        String purchaseToken,
        @NotBlank
        String subscriptionId
) {}
