package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;

public interface GetUserSubscriptionsUseCase {
    GetUserSubscriptionsResponse getUserSubscriptions(String email);
}
