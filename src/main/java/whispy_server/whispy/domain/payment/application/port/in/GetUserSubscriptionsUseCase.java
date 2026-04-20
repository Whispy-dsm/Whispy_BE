package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;

/**
 * 현재 사용자 구독 조회 유스케이스.
 */
public interface GetUserSubscriptionsUseCase {

    /**
     * email 기준 구독 정보를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 구독 응답
     */
    GetUserSubscriptionsResponse getUserSubscriptions(String email);

    /**
     * 현재 사용자 조회용 no-arg 진입점.
     *
     * @return 구독 응답
     */
    default GetUserSubscriptionsResponse getUserSubscriptions() {
        return getUserSubscriptions(null);
    }
}
