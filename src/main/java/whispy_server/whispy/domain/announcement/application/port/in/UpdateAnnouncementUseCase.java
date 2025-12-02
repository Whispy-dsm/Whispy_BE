package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 공지사항 수정 유스케이스.
 *
 * 기존 공지사항의 정보를 수정하는 인바운드 포트입니다.
 */
@UseCase
public interface UpdateAnnouncementUseCase {
    /**
     * 공지사항을 수정합니다.
     *
     * @param request 수정할 공지사항 정보가 포함된 요청
     */
    void execute(UpdateAnnouncementRequest request);
}
