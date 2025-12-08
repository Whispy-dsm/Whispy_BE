package whispy_server.whispy.domain.sleepsession.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.SleepSessionJpaEntity;

import java.util.Optional;

/**
 * 수면 세션 JPA 레포지토리.
 *
 * 수면 세션 엔티티에 대한 데이터베이스 조회 및 삭제 작업을 정의하는 인터페이스입니다.
 */
public interface SleepSessionRepository extends JpaRepository<SleepSessionJpaEntity, Long> {

    /**
     * 사용자 ID로 수면 세션을 시작 일시의 역순으로 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지 정보
     * @return 수면 세션 페이지
     */
    Page<SleepSessionJpaEntity> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);

    /**
     * 세션 ID와 사용자 ID로 특정 수면 세션을 조회합니다.
     *
     * @param id 조회할 세션 ID
     * @param userId 세션의 사용자 ID
     * @return 조회된 세션의 선택적 엔티티
     */
    Optional<SleepSessionJpaEntity> findByIdAndUserId(Long id, Long userId);

    /**
     * 사용자 ID로 해당 사용자의 모든 수면 세션을 삭제합니다.
     *
     * @param userId 삭제할 사용자의 ID
     */
    void deleteByUserId(Long userId);
}
