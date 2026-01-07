package whispy_server.whispy.domain.reason.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.reason.adapter.out.dto.WithdrawalStatisticsDto;
import whispy_server.whispy.domain.reason.adapter.out.entity.WithdrawalReasonJpaEntity;
import whispy_server.whispy.domain.reason.adapter.out.mapper.WithdrawalReasonMapper;
import whispy_server.whispy.domain.reason.adapter.out.persistence.repository.WithdrawalReasonJpaRepository;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonPort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import whispy_server.whispy.domain.reason.adapter.out.entity.QWithdrawalReasonJpaEntity;

/**
 * 탈퇴 사유 저장소 어댑터.
 */
@Component
@RequiredArgsConstructor
public class WithdrawalReasonPersistenceAdapter implements WithdrawalReasonPort {

    private final WithdrawalReasonJpaRepository withdrawalReasonJpaRepository;
    private final WithdrawalReasonMapper withdrawalReasonMapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 탈퇴 사유를 저장한다.
     */
    @Override
    public void save(WithdrawalReason withdrawalReason) {
        withdrawalReasonJpaRepository.save(withdrawalReasonMapper.toEntity(withdrawalReason));
    }

    /**
     * 탈퇴 사유 목록을 최신순으로 조회한다.
     */
    @Override
    public Page<WithdrawalReason> findAll(Pageable pageable) {
        return withdrawalReasonMapper.toPageModel(
                withdrawalReasonJpaRepository.findAllByOrderByCreatedAtDesc(pageable));
    }

    /**
     * ID로 탈퇴 사유를 조회한다.
     */
    @Override
    public Optional<WithdrawalReason> findById(Long id) {
        return withdrawalReasonMapper.toOptionalModel(withdrawalReasonJpaRepository.findById(id));
    }

    /**
     * ID 존재 여부를 확인한다.
     */
    @Override
    public boolean existsById(Long id) {
        return withdrawalReasonJpaRepository.existsById(id);
    }

    /**
     * 탈퇴 사유를 삭제한다.
     */
    @Override
    public void deleteById(Long id) {
        withdrawalReasonJpaRepository.deleteById(id);
    }

    /**
     * 날짜 범위 내의 탈퇴 사유를 조회한다.
     */
    @Override
    public Page<WithdrawalReason> findAllByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Page<WithdrawalReasonJpaEntity> entities = withdrawalReasonJpaRepository
                .findAllByCreatedAtBetweenOrderByCreatedAtDesc(startDateTime, endDateTime, pageable);

        return withdrawalReasonMapper.toPageModel(entities);
    }

    /**
     * 기간 내 날짜별 탈퇴 통계를 집계하여 조회한다.
     *
     * QueryDSL을 사용하여 데이터베이스에서 직접 GROUP BY 집계를 수행합니다.
     */
    @Override
    public List<WithdrawalStatisticsDto> aggregateDailyStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        QWithdrawalReasonJpaEntity withdrawalReason = QWithdrawalReasonJpaEntity.withdrawalReasonJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        WithdrawalStatisticsDto.class,
                        withdrawalReason.createdAt.year(),
                        withdrawalReason.createdAt.month(),
                        withdrawalReason.createdAt.dayOfMonth(),
                        withdrawalReason.count().intValue()))
                .from(withdrawalReason)
                .where(withdrawalReason.createdAt.between(startDateTime, endDateTime))
                .groupBy(
                        withdrawalReason.createdAt.year(),
                        withdrawalReason.createdAt.month(),
                        withdrawalReason.createdAt.dayOfMonth())
                .orderBy(
                        withdrawalReason.createdAt.year().asc(),
                        withdrawalReason.createdAt.month().asc(),
                        withdrawalReason.createdAt.dayOfMonth().asc())
                .fetch();
    }
}
