package whispy_server.whispy.domain.announcement.application.port.out;

/**
 * 공지사항 삭제 아웃바운드 포트.
 *
 * 공지사항 삭제 관련 영속성 기능을 정의하는 인터페이스입니다.
 */
public interface DeleteAnnouncementPort {
    /**
     * ID로 공지사항을 삭제합니다.
     *
     * @param id 삭제할 공지사항 ID
     */
    void deleteById(Long id);
}
