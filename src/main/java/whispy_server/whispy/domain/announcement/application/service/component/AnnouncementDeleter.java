package whispy_server.whispy.domain.announcement.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.application.port.out.DeleteAnnouncementPort;

/**
 * 공지사항 삭제 컴포넌트.
 *
 * 공지사항을 트랜잭션 내에서 삭제하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class AnnouncementDeleter {

    private final DeleteAnnouncementPort deleteAnnouncementPort;

    /**
     * 공지사항을 삭제합니다.
     *
     * @param id 삭제할 공지사항 ID
     */
    @Transactional
    public void delete(Long id) {
        deleteAnnouncementPort.deleteById(id);
    }
}
