package whispy_server.whispy.domain.announcement.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.announcement.adapter.out.mapper.AnnouncementMapper;
import whispy_server.whispy.domain.announcement.adapter.out.persistence.repository.AnnouncementJpaRepository;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;
import java.util.Optional;

/**
 * 공지사항 영속성 어댑터.
 *
 * 공지사항 도메인의 데이터베이스 접근을 담당하는 아웃바운드 어댑터입니다.
 * AnnouncementPort를 구현하여 도메인 계층에 영속성 기능을 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class AnnouncementPersistenceAdapter implements AnnouncementPort {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final AnnouncementMapper announcementMapper;

    /**
     * 공지사항을 저장합니다.
     *
     * @param announcement 저장할 공지사항 도메인 모델
     * @return 저장된 공지사항 도메인 모델
     */
    @Override
    public Announcement save(Announcement announcement) {
        return announcementMapper.toModel(
                announcementJpaRepository.save(announcementMapper.toEntity(announcement))
        );
    }

    /**
     * ID로 공지사항을 조회합니다.
     *
     * @param id 조회할 공지사항 ID
     * @return Optional 공지사항 도메인 모델
     */
    @Override
    public Optional<Announcement> findById(Long id) {
        return announcementMapper.toOptionalModel(announcementJpaRepository.findById(id));
    }

    /**
     * 모든 공지사항을 생성일시 기준 내림차순으로 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 공지사항 도메인 모델 페이지
     */
    @Override
    public Page<Announcement> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        return announcementMapper.toModelPage(announcementJpaRepository.findAllByOrderByCreatedAtDesc(pageable));
    }

    /**
     * ID로 공지사항을 삭제합니다.
     *
     * @param id 삭제할 공지사항 ID
     */
    @Override
    public void deleteById(Long id) {
        announcementJpaRepository.deleteById(id);
    }
}
