package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.in.UpdateAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

/**
 * 공지사항 수정 서비스.
 *
 * 기존 공지사항의 정보를 수정하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class UpdateAnnouncementService implements UpdateAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    /**
     * 공지사항을 수정합니다.
     *
     * @param request 수정할 공지사항 정보가 포함된 요청
     * @throws AnnouncementNotFoundException 공지사항을 찾을 수 없는 경우
     */
    @AdminAction("공지사항 수정")
    @Transactional
    @Override
    public void execute(UpdateAnnouncementRequest request) {
        Announcement announcement = announcementPort.findById(request.id())
                .orElseThrow(() -> AnnouncementNotFoundException.EXCEPTION);
        
        Announcement updatedAnnouncement = announcement.update(request);
        
        announcementPort.save(updatedAnnouncement);
    }
}
