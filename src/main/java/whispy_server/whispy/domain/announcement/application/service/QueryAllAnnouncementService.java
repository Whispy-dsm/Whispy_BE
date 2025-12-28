package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAllAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 모든 공지사항 조회 서비스.
 *
 * 전체 공지사항 목록을 페이지네이션하여 조회하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class QueryAllAnnouncementService implements QueryAllAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    /**
     * 모든 공지사항 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 공지사항 목록 (생성일시 기준 내림차순 정렬)
     */
    @UserAction("공지사항 목록 조회")
    @Transactional(readOnly = true)
    @Override
    public Page<QueryAllAnnouncementResponse> execute(Pageable pageable) {
        return announcementPort.findAllByOrderByCreatedAtDesc(pageable)
                .map(QueryAllAnnouncementResponse::from);
    }
}