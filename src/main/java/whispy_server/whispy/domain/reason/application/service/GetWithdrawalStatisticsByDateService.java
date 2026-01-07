package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalStatisticsByDateResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalStatisticsByDateUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.exception.domain.statistics.DateRangeExceededException;
import whispy_server.whispy.global.exception.domain.statistics.InvalidDateRangeException;
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 날짜별 탈퇴 통계 조회 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalStatisticsByDateService implements GetWithdrawalStatisticsByDateUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    private static final long MAX_DAYS = 365; // 최대 1년

    /**
     * 기간 내 날짜별 탈퇴 통계를 조회합니다.
     *
     * QueryDSL을 통해 데이터베이스에서 직접 집계된 결과를 반환합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 날짜별 탈퇴 통계 목록
     * @throws InvalidStatisticsDateException 미래 날짜를 조회하려는 경우
     * @throws InvalidDateRangeException 시작 날짜가 종료 날짜보다 늦은 경우
     * @throws DateRangeExceededException 조회 기간이 1년을 초과하는 경우
     */
    @AdminAction("날짜별 탈퇴 통계 조회")
    @Transactional(readOnly = true)
    @Override
    public List<WithdrawalStatisticsByDateResponse> execute(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        return withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate)
                .stream()
                .map(dto -> new WithdrawalStatisticsByDateResponse(dto.date(), dto.count()))
                .toList();
    }

    /**
     * 날짜 범위를 검증합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @throws InvalidStatisticsDateException 미래 날짜를 조회하려는 경우
     * @throws InvalidDateRangeException 시작 날짜가 종료 날짜보다 늦은 경우
     * @throws DateRangeExceededException 조회 기간이 1년을 초과하는 경우
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        // 미래 날짜 검증
        LocalDate today = LocalDate.now();
        if (startDate.isAfter(today) || endDate.isAfter(today)) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }

        // 시작 날짜가 종료 날짜보다 늦은지 검증
        if (startDate.isAfter(endDate)) {
            throw InvalidDateRangeException.EXCEPTION;
        }

        // 날짜 범위가 1년을 초과하는지 검증
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > MAX_DAYS) {
            throw DateRangeExceededException.EXCEPTION;
        }
    }
}
