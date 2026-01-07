package whispy_server.whispy.domain.reason.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.reason.adapter.out.entity.WithdrawalReasonJpaEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 탈퇴 사유 엔터티 리포지토리.
 */
public interface WithdrawalReasonJpaRepository extends JpaRepository<WithdrawalReasonJpaEntity, Long> {
    /**
     * 생성일 내림차순으로 페이지 조회한다.
     */
    Page<WithdrawalReasonJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 특정 날짜 범위의 탈퇴 이유를 조회한다.
     *
     * @param startOfDay 시작 시간
     * @param endOfDay 종료 시간
     * @param pageable 페이지 정보
     * @return 탈퇴 이유 페이지
     */
    Page<WithdrawalReasonJpaEntity> findAllByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            Pageable pageable
    );
}
