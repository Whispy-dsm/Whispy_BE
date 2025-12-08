package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;

/**
 * 사용자 구독 상태 확인 유스케이스.
 *
 * 사용자의 현재 구독 상태를 확인하는 인바운드 포트입니다.
 */
public interface CheckUserSubscriptionStatusUseCase {
    /**
     * 사용자의 구독 상태를 확인합니다.
     *
     * @param email 사용자 이메일
     * @return 구독 상태 정보
     */
    CheckUserSubscriptionStatusResponse isUserSubscribed(String email);
}
