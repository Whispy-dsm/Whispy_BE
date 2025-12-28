package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.application.port.in.CheckUserSubscriptionStatusUseCase;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.Optional;

/**
 * 사용자 구독 상태 확인 서비스.
 *
 * 사용자의 현재 구독 상태를 확인하는 유스케이스 구현체입니다.
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CheckUserSubscriptionStatusService implements CheckUserSubscriptionStatusUseCase {

    private final QuerySubscriptionPort querySubscriptionPort;

    /**
     * 사용자의 구독 상태를 확인합니다.
     *
     * @param email 사용자 이메일
     * @return 구독 상태 정보
     */
    @UserAction("구독 상태 확인")
    @Override
    public CheckUserSubscriptionStatusResponse isUserSubscribed(String email) {

        Optional<Subscription> activeSubscription = querySubscriptionPort.findActiveSubscriptionByEmail(email);
        boolean isSubscribed = activeSubscription
                .map(Subscription::isActive)
                .orElse(false);

        return new CheckUserSubscriptionStatusResponse(isSubscribed);
    }
}
