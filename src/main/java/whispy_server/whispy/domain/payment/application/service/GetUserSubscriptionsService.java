package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.SubscriptionSummaryResponse;
import whispy_server.whispy.domain.payment.application.port.in.GetUserSubscriptionsUseCase;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.Optional;

/**
 * 현재 사용자 구독 정보 조회 서비스.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserSubscriptionsService implements GetUserSubscriptionsUseCase {

    private final QuerySubscriptionPort querySubscriptionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 인증된 사용자의 구독 요약 정보를 조회한다.
     *
     * @param ignoredEmail 이전 계약과의 호환용 파라미터
     * @return 현재 사용자 구독 응답
     */
    @UserAction("구독 정보 조회")
    @Override
    public GetUserSubscriptionsResponse getUserSubscriptions(String ignoredEmail) {
        User currentUser = userFacadeUseCase.currentUser();
        Optional<SubscriptionSummaryResponse> subscriptions = querySubscriptionPort.findByEmail(currentUser.email())
                .map(SubscriptionSummaryResponse::from);
        return new GetUserSubscriptionsResponse(subscriptions);
    }

    public GetUserSubscriptionsResponse getUserSubscriptions() {
        return getUserSubscriptions(null);
    }
}
