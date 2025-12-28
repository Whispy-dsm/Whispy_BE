package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.application.port.in.GetUserSubscriptionsUseCase;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.Optional;

/**
 * 사용자 구독 정보 조회 서비스.
 *
 * 사용자의 모든 구독 정보를 조회하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserSubscriptionsService implements GetUserSubscriptionsUseCase {

    private final QuerySubscriptionPort querySubscriptionPort;

    /**
     * 사용자의 모든 구독 정보를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 사용자의 구독 정보 목록
     */
    @UserAction("구독 정보 조회")
    @Override
    public GetUserSubscriptionsResponse getUserSubscriptions(String email) {
        Optional<Subscription> subscriptions = querySubscriptionPort.findByEmail(email);
        return new GetUserSubscriptionsResponse(subscriptions);
    }
}
