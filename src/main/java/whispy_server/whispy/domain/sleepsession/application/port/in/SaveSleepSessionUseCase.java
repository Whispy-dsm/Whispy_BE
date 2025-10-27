package whispy_server.whispy.domain.sleepsession.application.port.in;

import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request.SaveSleepSessionRequest;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SaveSleepSessionUseCase {
    SleepSessionResponse execute(SaveSleepSessionRequest request);
}
