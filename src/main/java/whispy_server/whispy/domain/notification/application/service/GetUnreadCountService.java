package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.notification.application.port.in.GetUnreadCountUseCase;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUnreadCountService implements GetUnreadCountUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public UnreadCountResponse execute(){
        int count = queryNotificationPort.countByEmailAndIsReadFalse(
                userFacadeUseCase.currentUser().email()
        );

        return new UnreadCountResponse(count);
    }
}
