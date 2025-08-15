package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.UnreadCountResponse;

public interface GetUnReadCountUseCase {
    UnreadCountResponse execute();
}
