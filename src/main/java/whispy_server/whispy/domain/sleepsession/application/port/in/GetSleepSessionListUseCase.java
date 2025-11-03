package whispy_server.whispy.domain.sleepsession.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionListResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetSleepSessionListUseCase {
    Page<SleepSessionListResponse> execute(Pageable pageable);
}
