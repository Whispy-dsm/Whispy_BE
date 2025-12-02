package whispy_server.whispy.domain.announcement.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;

import java.util.List;

/**
 * 모든 공지사항 조회 유스케이스.
 *
 * 전체 공지사항 목록을 페이지네이션하여 조회하는 인바운드 포트입니다.
 */
public interface QueryAllAnnouncementUseCase {
    /**
     * 모든 공지사항 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 공지사항 목록
     */
    Page<QueryAllAnnouncementResponse> execute(Pageable pageable);
}
