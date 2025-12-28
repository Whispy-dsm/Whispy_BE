package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.notification.application.port.in.GetUnreadCountUseCase;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 읽지 않은 알림 개수 조회 서비스.
 *
 * 현재 사용자의 읽지 않은 알림 개수를 조회하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUnreadCountService implements GetUnreadCountUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 읽지 않은 알림 개수를 조회합니다.
     *
     * @return 읽지 않은 알림 개수 응답
     */
    @UserAction("읽지 않은 알림 개수 조회")
    @Override
    public UnreadCountResponse execute(){
        int count = queryNotificationPort.countByEmailAndIsReadFalse(
                userFacadeUseCase.currentUser().email()
        );

        return new UnreadCountResponse(count);
    }
}
