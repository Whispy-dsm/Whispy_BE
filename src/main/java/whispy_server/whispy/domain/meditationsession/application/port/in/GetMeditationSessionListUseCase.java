package whispy_server.whispy.domain.meditationsession.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionListResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetMeditationSessionListUseCase {
    Page<MeditationSessionListResponse> execute(Pageable pageable);
}
