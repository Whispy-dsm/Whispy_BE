package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.application.port.in.GetUserSubscriptionsUseCase;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserSubscriptionsService implements GetUserSubscriptionsUseCase {

    private final QuerySubscriptionPort querySubscriptionPort;

    @Override
    public GetUserSubscriptionsResponse getUserSubscriptions(String email) {
        Optional<Subscription> subscriptions = querySubscriptionPort.findByEmail(email);
        return new GetUserSubscriptionsResponse(subscriptions);
    }
}
