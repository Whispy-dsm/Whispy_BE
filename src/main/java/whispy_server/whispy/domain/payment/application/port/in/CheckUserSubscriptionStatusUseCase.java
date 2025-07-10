package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;

public interface CheckUserSubscriptionStatusUseCase {
    CheckUserSubscriptionStatusResponse isUserSubscribed(String email);
}
