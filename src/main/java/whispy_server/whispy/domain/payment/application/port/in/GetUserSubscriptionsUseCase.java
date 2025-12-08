package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;

/**
 * 사용자 구독 정보 조회 유스케이스.
 *
 * 사용자의 모든 구독 정보를 조회하는 인바운드 포트입니다.
 */
public interface GetUserSubscriptionsUseCase {
    /**
     * 사용자의 모든 구독 정보를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 사용자의 구독 정보 목록
     */
    GetUserSubscriptionsResponse getUserSubscriptions(String email);
}
