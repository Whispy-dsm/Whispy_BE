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
import whispy_server.whispy.global.document.api.payment.SubscriptionApiDocument;

/**
 * 구독 REST 컨트롤러.
 *
 * 사용자 구독 정보 조회 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionApiDocument {

    private final GetUserSubscriptionsUseCase getUserSubscriptionsUseCase;
    private final CheckUserSubscriptionStatusUseCase checkUserSubscriptionStatusUseCase;

    /**
     * 사용자의 구독 정보를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 사용자의 구독 정보
     */
    @GetMapping("/user/{email}")
    public GetUserSubscriptionsResponse getUserSubscriptions(@PathVariable String email) {
        return getUserSubscriptionsUseCase.getUserSubscriptions(email);
    }

    /**
     * 사용자의 구독 상태를 확인합니다.
     *
     * @param email 사용자 이메일
     * @return 구독 상태 정보
     */
    @GetMapping("/user/{email}/status")
    public CheckUserSubscriptionStatusResponse isUserSubscribed(@PathVariable String email) {
        return checkUserSubscriptionStatusUseCase.isUserSubscribed(email);
    }
}
