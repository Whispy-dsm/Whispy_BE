package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UpdateAnnouncementUseCase {
    void execute(UpdateAnnouncementRequest request);
}
