package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface CreateAnnouncementUseCase {
    void execute(CreateAnnouncementRequest request);
}
