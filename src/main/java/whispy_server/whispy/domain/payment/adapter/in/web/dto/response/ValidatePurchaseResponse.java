package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

public record ValidatePurchaseResponse(
        boolean isValid,
        String message
) {}
