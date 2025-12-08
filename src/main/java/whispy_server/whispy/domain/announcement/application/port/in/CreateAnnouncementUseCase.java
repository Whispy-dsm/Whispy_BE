package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 공지사항 생성 유스케이스.
 *
 * 새로운 공지사항을 생성하는 인바운드 포트입니다.
 */
@UseCase
public interface CreateAnnouncementUseCase {
    /**
     * 공지사항을 생성합니다.
     *
     * @param request 생성할 공지사항 정보가 포함된 요청
     */
    void execute(CreateAnnouncementRequest request);
}
