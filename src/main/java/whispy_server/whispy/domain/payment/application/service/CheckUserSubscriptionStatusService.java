package whispy_server.whispy.domain.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.application.port.in.CheckUserSubscriptionStatusUseCase;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.Optional;

/**
 * 현재 사용자 구독 상태 조회 서비스.
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CheckUserSubscriptionStatusService implements CheckUserSubscriptionStatusUseCase {

    private final QuerySubscriptionPort querySubscriptionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 인증된 사용자의 entitlement 상태를 조회한다.
     *
     * @param ignoredEmail 이전 계약과의 호환용 파라미터
     * @return 현재 사용자 구독 상태 응답
     */
    @UserAction("구독 상태 확인")
    @Override
    public CheckUserSubscriptionStatusResponse isUserSubscribed(String ignoredEmail) {
        User currentUser = userFacadeUseCase.currentUser();
        Optional<Subscription> currentSubscription = querySubscriptionPort.findCurrentSubscriptionByEmail(currentUser.email());
        boolean isSubscribed = currentSubscription
                .map(Subscription::isActive)
                .orElse(false);

        return new CheckUserSubscriptionStatusResponse(isSubscribed);
    }

    public CheckUserSubscriptionStatusResponse isUserSubscribed() {
        return isUserSubscribed(null);
    }
}
