package whispy_server.whispy.domain.notification.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;

import java.util.List;

/**
 * 내 알림 목록 조회 유스케이스.
 *
 * 사용자의 알림 목록을 페이지네이션하여 조회하는 인바운드 포트입니다.
 */
public interface QueryMyNotificationsUseCase {
    /**
     * 내 알림 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 알림 목록 페이지
     */
    Page<NotificationResponse> execute(Pageable pageable);
}
