package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;

public interface GetUnreadCountUseCase {
    UnreadCountResponse execute();
}
