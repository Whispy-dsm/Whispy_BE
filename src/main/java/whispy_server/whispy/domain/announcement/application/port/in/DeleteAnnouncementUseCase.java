package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface DeleteAnnouncementUseCase {
    void execute(Long id);
}
