package whispy_server.whispy.domain.reason.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.reason.adapter.out.entity.WithdrawalReasonJpaEntity;

/**
 * 탈퇴 사유 엔터티 리포지토리.
 */
public interface WithdrawalReasonJpaRepository extends JpaRepository<WithdrawalReasonJpaEntity, Long> {
    /**
     * 생성일 내림차순으로 페이지 조회한다.
     */
    Page<WithdrawalReasonJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
