package whispy_server.whispy.domain.announcement.application.port.out;

/**
 * 공지사항 아웃바운드 포트.
 *
 * 공지사항 도메인의 모든 아웃바운드 포트를 통합한 인터페이스입니다.
 * SaveAnnouncementPort, QueryAnnouncementPort, DeleteAnnouncementPort를 상속합니다.
 */
public interface AnnouncementPort extends SaveAnnouncementPort, QueryAnnouncementPort, DeleteAnnouncementPort {
}
