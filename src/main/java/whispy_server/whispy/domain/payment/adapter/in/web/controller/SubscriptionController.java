package whispy_server.whispy.domain.payment.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.application.port.in.CheckUserSubscriptionStatusUseCase;
import whispy_server.whispy.domain.payment.application.port.in.GetUserSubscriptionsUseCase;
import whispy_server.whispy.global.document.api.payment.SubscriptionApiDocument;

/**
 * 현재 사용자 구독 조회 컨트롤러.
 */
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionApiDocument {

    private final GetUserSubscriptionsUseCase getUserSubscriptionsUseCase;
    private final CheckUserSubscriptionStatusUseCase checkUserSubscriptionStatusUseCase;

    /**
     * 현재 인증된 사용자의 구독 정보를 조회한다.
     *
     * @return 현재 사용자 구독 응답
     */
    @GetMapping("/me")
    public GetUserSubscriptionsResponse getUserSubscriptions() {
        return getUserSubscriptionsUseCase.getUserSubscriptions();
    }

    /**
     * 현재 인증된 사용자의 구독 상태를 조회한다.
     *
     * @return 현재 사용자 구독 상태 응답
     */
    @GetMapping("/me/status")
    public CheckUserSubscriptionStatusResponse isUserSubscribed() {
        return checkUserSubscriptionStatusUseCase.isUserSubscribed();
    }
}
