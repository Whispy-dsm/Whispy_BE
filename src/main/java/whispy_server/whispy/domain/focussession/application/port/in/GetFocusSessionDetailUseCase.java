package whispy_server.whispy.domain.focussession.application.port.in;

import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetFocusSessionDetailUseCase {
    FocusSessionDetailResponse execute(Long focusSessionId);
}
