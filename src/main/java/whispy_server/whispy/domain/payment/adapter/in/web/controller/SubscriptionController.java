package whispy_server.whispy.domain.payment.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.application.port.in.CheckUserSubscriptionStatusUseCase;
import whispy_server.whispy.domain.payment.application.port.in.GetUserSubscriptionsUseCase;


@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final GetUserSubscriptionsUseCase getUserSubscriptionsUseCase;
    private final CheckUserSubscriptionStatusUseCase checkUserSubscriptionStatusUseCase;

    @GetMapping("/user/{email}")
    public GetUserSubscriptionsResponse getUserSubscriptions(@PathVariable String email) {
        return getUserSubscriptionsUseCase.getUserSubscriptions(email);
    }

    @GetMapping("/user/{email}/status")
    public CheckUserSubscriptionStatusResponse isUserSubscribed(@PathVariable String email) {
        return checkUserSubscriptionStatusUseCase.isUserSubscribed(email);
    }
}
