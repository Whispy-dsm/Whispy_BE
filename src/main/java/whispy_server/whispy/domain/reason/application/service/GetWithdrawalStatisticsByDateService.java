package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalStatisticsByDateResponse;
import whispy_server.whispy.domain.reason.adapter.out.dto.WithdrawalStatisticsDto;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalStatisticsByDateUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.exception.domain.statistics.DateRangeExceededException;
import whispy_server.whispy.global.exception.domain.statistics.InvalidDateRangeException;
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 날짜별 탈퇴 통계 조회 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalStatisticsByDateService implements GetWithdrawalStatisticsByDateUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    private static final int MAX_YEARS = 1; // 최대 1년

    /**
     * 기간 내 날짜별 탈퇴 통계를 조회합니다.
     *
     * QueryDSL을 통해 데이터베이스에서 직접 집계된 결과를 반환합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @return 날짜별 탈퇴 통계 목록
     * @throws InvalidStatisticsDateException 미래 날짜를 조회하려는 경우
     * @throws InvalidDateRangeException      시작 날짜가 종료 날짜보다 늦은 경우
     * @throws DateRangeExceededException     조회 기간이 1년을 초과하는 경우
     */
    @AdminAction("날짜별 탈퇴 통계 조회")
    @Transactional(readOnly = true)
    @Override
    public List<WithdrawalStatisticsByDateResponse> execute(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        Map<LocalDate, Integer> statisticsMap = withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        WithdrawalStatisticsDto::date,
                        WithdrawalStatisticsDto::count));

        return startDate.datesUntil(endDate.plusDays(1))
                .map(date -> new WithdrawalStatisticsByDateResponse(
                        date,
                        statisticsMap.getOrDefault(date, 0)))
                .toList();
    }

    /**
     * 날짜 범위를 검증합니다.
     *
     * 검증 항목:
     * 1. 미래 날짜 조회 불가
     * 2. 시작 날짜 <= 종료 날짜
     * 3. 조회 기간 <= 1년 (윤년 고려)
     *
     * 예시:
     * - startDate: 2024-01-01, endDate: 2024-12-31 → 통과
     * - startDate: 2024-01-01, endDate: 2025-01-02 → 실패 (1년 초과)
     * - startDate: 2024-02-29, endDate: 2025-02-28 → 통과 (윤년 1년)
     *
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @throws InvalidStatisticsDateException 미래 날짜를 조회하려는 경우
     * @throws InvalidDateRangeException      시작 날짜가 종료 날짜보다 늦은 경우
     * @throws DateRangeExceededException     조회 기간이 1년을 초과하는 경우
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

        // 날짜 범위가 1년을 초과하는지 검증 (윤년 고려)
        long yearsBetween = ChronoUnit.YEARS.between(startDate, endDate);
        if (yearsBetween > MAX_YEARS) {
            throw DateRangeExceededException.EXCEPTION;
        }
        // 정확히 1년인 경우 endDate가 startDate보다 1년 후를 넘는지 확인
        if (yearsBetween == MAX_YEARS && endDate.isAfter(startDate.plusYears(MAX_YEARS))) {
            throw DateRangeExceededException.EXCEPTION;
        }
    }
}
