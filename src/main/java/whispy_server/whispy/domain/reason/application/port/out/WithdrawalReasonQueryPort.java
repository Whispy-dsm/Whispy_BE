package whispy_server.whispy.domain.reason.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.adapter.out.dto.WithdrawalStatisticsDto;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 탈퇴 사유 조회 Port.
 */
public interface WithdrawalReasonQueryPort {
    /**
     * 탈퇴 사유 목록을 페이지로 조회한다.
     */
    Page<WithdrawalReason> findAll(Pageable pageable);

    /**
     * ID로 탈퇴 사유를 조회한다.
     */
    Optional<WithdrawalReason> findById(Long id);

    /**
     * ID 존재 여부를 확인한다.
     */
    boolean existsById(Long id);

    /**
     * 특정 날짜의 탈퇴 사유를 페이지로 조회한다.
     *
     * @param date 조회할 날짜
     * @param pageable 페이지 정보
     * @return 탈퇴 사유 페이지
     */
    Page<WithdrawalReason> findAllByDate(LocalDate date, Pageable pageable);

    /**
     * 기간 내 날짜별 탈퇴 통계를 집계하여 조회한다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 날짜별 탈퇴 건수 집계 결과
     */
    List<WithdrawalStatisticsDto> aggregateDailyStatistics(LocalDate startDate, LocalDate endDate);
}
