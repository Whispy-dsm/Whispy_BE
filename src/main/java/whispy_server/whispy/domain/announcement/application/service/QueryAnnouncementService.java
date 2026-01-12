package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

/**
 * 공지사항 조회 서비스.
 *
 * 특정 공지사항의 상세 정보를 조회하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class QueryAnnouncementService implements QueryAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    /**
     * 공지사항 상세 정보를 조회합니다.
     *
     * @param id 조회할 공지사항 ID
     * @return 공지사항 상세 정보
     * @throws AnnouncementNotFoundException 공지사항을 찾을 수 없는 경우
     */
    @UserAction("공지사항 상세 조회")
    @Transactional(readOnly = true)
    @Override
    public QueryAnnouncementResponse execute(Long id) {
        Announcement announcement = announcementPort.findById(id)
                .orElseThrow(() -> AnnouncementNotFoundException.EXCEPTION);

        return new QueryAnnouncementResponse(
                announcement.title(),
                announcement.content()
        );
    }
}
