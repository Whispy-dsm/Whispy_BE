package whispy_server.whispy.domain.announcement.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;
import java.util.Optional;

/**
 * 공지사항 조회 아웃바운드 포트.
 *
 * 공지사항 조회 관련 영속성 기능을 정의하는 인터페이스입니다.
 */
public interface QueryAnnouncementPort {
    /**
     * ID로 공지사항을 조회합니다.
     *
     * @param id 조회할 공지사항 ID
     * @return Optional 공지사항 도메인 모델
     */
    Optional<Announcement> findById(Long id);

    /**
     * 모든 공지사항을 생성일시 기준 내림차순으로 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 공지사항 도메인 모델 페이지
     */
    Page<Announcement> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
