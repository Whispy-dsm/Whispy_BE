package whispy_server.whispy.domain.announcement.application.port.out;

import whispy_server.whispy.domain.announcement.model.Announcement;

/**
 * 공지사항 저장 아웃바운드 포트.
 *
 * 공지사항 저장 관련 영속성 기능을 정의하는 인터페이스입니다.
 */
public interface SaveAnnouncementPort {
    /**
     * 공지사항을 저장합니다.
     *
     * @param announcement 저장할 공지사항 도메인 모델
     * @return 저장된 공지사항 도메인 모델
     */
    Announcement save(Announcement announcement);
}
