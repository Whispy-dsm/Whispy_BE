package whispy_server.whispy.domain.meditationsession.application.port.in;

import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetMeditationSessionDetailUseCase {
    MeditationSessionDetailResponse execute(Long meditationSessionId);
}
