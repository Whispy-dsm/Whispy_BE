package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 공지사항 삭제 유스케이스.
 *
 * 특정 공지사항을 삭제하는 인바운드 포트입니다.
 */
@UseCase
public interface DeleteAnnouncementUseCase {
    /**
     * 공지사항을 삭제합니다.
     *
     * @param id 삭제할 공지사항 ID
     */
    void execute(Long id);
}
