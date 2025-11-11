package whispy_server.whispy.domain.meditationsession.application.port.in;

import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request.SaveMeditationSessionRequest;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SaveMeditationSessionUseCase {
    MeditationSessionResponse execute(SaveMeditationSessionRequest request);
}
