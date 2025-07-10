package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.application.port.in.CheckUserSubscriptionStatusUseCase;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CheckUserSubscriptionStatusService implements CheckUserSubscriptionStatusUseCase {

    private final QuerySubscriptionPort querySubscriptionPort;

    @Override
    public CheckUserSubscriptionStatusResponse isUserSubscribed(String email) {

        Optional<Subscription> activeSubscription = querySubscriptionPort.findActiveSubscriptionByEmail(email);
        boolean isSubscribed = activeSubscription
                .map(Subscription::isActive)
                .orElse(false);

        return new CheckUserSubscriptionStatusResponse(isSubscribed);
    }
}
