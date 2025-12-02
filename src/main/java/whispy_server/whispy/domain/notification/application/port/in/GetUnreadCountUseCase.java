package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;

/**
 * 안읽은 알림 개수 조회 유스케이스.
 *
 * 사용자의 안읽은 알림 개수를 조회하는 인바운드 포트입니다.
 */
public interface GetUnreadCountUseCase {
    /**
     * 안읽은 알림 개수를 조회합니다.
     *
     * @return 안읽은 알림 개수 응답
     */
    UnreadCountResponse execute();
}
