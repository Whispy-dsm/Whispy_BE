package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface BroadCastToAllUsersUseCase {
    void execute(FcmSendRequest request);
}
