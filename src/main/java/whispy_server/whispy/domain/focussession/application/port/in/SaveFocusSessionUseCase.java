package whispy_server.whispy.domain.focussession.application.port.in;

import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SaveFocusSessionUseCase {
    FocusSessionResponse execute(SaveFocusSessionRequest request);
}
