package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

public record GetUserSubscriptionsResponse(
        Optional<Subscription> subscriptions
) {}
