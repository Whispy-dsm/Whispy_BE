package whispy_server.whispy.domain.announcement.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;

/**
 * 공지사항 저장 컴포넌트.
 *
 * 공지사항을 트랜잭션 내에서 저장하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class AnnouncementSaver {

    private final AnnouncementPort announcementPort;

    /**
     * 공지사항을 저장합니다.
     *
     * @param announcement 저장할 공지사항
     * @return 저장된 공지사항의 ID
     */
    @Transactional
    public Long save(Announcement announcement) {
        Announcement saved = announcementPort.save(announcement);
        return saved.id();
    }
}
