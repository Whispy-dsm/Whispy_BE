package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.application.port.in.DeleteAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;

/**
 * 공지사항 삭제 서비스.
 *
 * 특정 공지사항을 삭제하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class DeleteAnnouncementService implements DeleteAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    /**
     * 공지사항을 삭제합니다.
     *
     * @param id 삭제할 공지사항 ID
     */
    @Transactional
    @Override
    public void execute(Long id) {
        announcementPort.deleteById(id);
    }
}