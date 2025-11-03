package whispy_server.whispy.domain.sleepsession.application.port.in;

import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetSleepSessionDetailUseCase {
    SleepSessionDetailResponse execute(Long sleepSessionId);
}
