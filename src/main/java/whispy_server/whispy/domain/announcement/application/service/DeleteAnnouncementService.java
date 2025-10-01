package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.application.port.in.DeleteAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;

@Service
@RequiredArgsConstructor
public class DeleteAnnouncementService implements DeleteAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    @Transactional
    @Override
    public void execute(Long id) {
        announcementPort.deleteById(id);
    }
}