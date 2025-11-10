package whispy_server.whispy.domain.reason.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.reason.adapter.out.entity.WithdrawalReasonJpaEntity;

public interface WithdrawalReasonJpaRepository extends JpaRepository<WithdrawalReasonJpaEntity, Long> {
    Page<WithdrawalReasonJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
