package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface QueryAnnouncementUseCase {
    QueryAnnouncementResponse execute(Long id);
}
