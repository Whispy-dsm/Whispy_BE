package whispy_server.whispy.domain.announcement.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAllAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAnnouncementUseCase;
import whispy_server.whispy.global.document.api.announcement.AnnouncementApiDocument;

/**
 * 공지사항 REST 컨트롤러.
 *
 * 공지사항 조회 기능을 제공하는 인바운드 어댑터입니다.
 * 사용자는 모든 공지사항 목록과 개별 공지사항 상세 정보를 조회할 수 있습니다.
 */
@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController implements AnnouncementApiDocument {

    private final QueryAnnouncementUseCase queryAnnouncementUseCase;
    private final QueryAllAnnouncementUseCase queryAllAnnouncementUseCase;

    /**
     * 특정 공지사항의 상세 정보를 조회합니다.
     *
     * @param id 조회할 공지사항 ID
     * @return 공지사항 상세 정보
     */
    @GetMapping("/{id}")
    @Override
    public QueryAnnouncementResponse getAnnouncement(@PathVariable Long id) {
        return queryAnnouncementUseCase.execute(id);
    }

    /**
     * 모든 공지사항 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 공지사항 목록
     */
    @GetMapping
    @Override
    public Page<QueryAllAnnouncementResponse> getAllAnnouncements(Pageable pageable) {
        return queryAllAnnouncementUseCase.execute(pageable);
    }
}
