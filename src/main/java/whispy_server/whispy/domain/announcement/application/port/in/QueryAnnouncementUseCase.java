package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 공지사항 조회 유스케이스.
 *
 * 특정 공지사항의 상세 정보를 조회하는 인바운드 포트입니다.
 */
@UseCase
public interface QueryAnnouncementUseCase {
    /**
     * 공지사항 상세 정보를 조회합니다.
     *
     * @param id 조회할 공지사항 ID
     * @return 공지사항 상세 정보
     */
    QueryAnnouncementResponse execute(Long id);
}
