package whispy_server.whispy.domain.reason.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalStatisticsByDateResponse;
import whispy_server.whispy.domain.reason.adapter.out.dto.WithdrawalStatisticsDto;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.application.service.GetWithdrawalStatisticsByDateService;
import whispy_server.whispy.global.exception.domain.statistics.DateRangeExceededException;
import whispy_server.whispy.global.exception.domain.statistics.InvalidDateRangeException;
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * GetWithdrawalStatisticsByDateService의 단위 테스트 클래스
 *
 * 날짜별 탈퇴 통계 조회 서비스의 다양한 시나리오를 검증합니다.
 * 날짜 범위 검증 및 통계 집계 로직을 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetWithdrawalStatisticsByDateService 테스트")
class GetWithdrawalStatisticsByDateServiceTest {

    @InjectMocks
    private GetWithdrawalStatisticsByDateService service;

    @Mock
    private WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 테스트용 WithdrawalStatisticsDto 객체를 생성합니다.
     *
     * @param date 날짜
     * @param count 탈퇴 건수
     * @return 생성된 WithdrawalStatisticsDto 객체
     */
    private WithdrawalStatisticsDto createStatistics(LocalDate date, Long count) {
        return new WithdrawalStatisticsDto(date, count);
    }

    @Test
    @DisplayName("기간 내 날짜별 탈퇴 통계를 조회할 수 있다")
    void whenQueryingStatistics_thenReturnsResults() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 7);

        List<WithdrawalStatisticsDto> statistics = List.of(
                createStatistics(LocalDate.of(2024, 1, 1), 3L),
                createStatistics(LocalDate.of(2024, 1, 3), 5L),
                createStatistics(LocalDate.of(2024, 1, 5), 2L),
                createStatistics(LocalDate.of(2024, 1, 7), 4L)
        );

        given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                .willReturn(statistics);

        // when
        List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

        // then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.get(0).count()).isEqualTo(3L);
        assertThat(result.get(1).count()).isEqualTo(5L);
    }

    @Test
    @DisplayName("탈퇴가 없는 날짜는 결과에 포함되지 않는다")
    void whenNoWithdrawalsOnSomeDates_thenReturnsOnlyDatesWithData() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 10);

        // 1월 1일과 1월 5일만 탈퇴가 있음
        List<WithdrawalStatisticsDto> statistics = List.of(
                createStatistics(LocalDate.of(2024, 1, 1), 2L),
                createStatistics(LocalDate.of(2024, 1, 5), 3L)
        );

        given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                .willReturn(statistics);

        // when
        List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.get(1).date()).isEqualTo(LocalDate.of(2024, 1, 5));
    }

    @Test
    @DisplayName("시작 날짜가 종료 날짜보다 늦으면 예외가 발생한다")
    void whenStartDateAfterEndDate_thenThrowsException() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 10);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        // when & then
        assertThatThrownBy(() -> service.execute(startDate, endDate))
                .isInstanceOf(InvalidDateRangeException.class);
    }

    @Test
    @DisplayName("시작 날짜가 미래이면 예외가 발생한다")
    void whenStartDateInFuture_thenThrowsException() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> service.execute(startDate, endDate))
                .isInstanceOf(InvalidStatisticsDateException.class);
    }

    @Test
    @DisplayName("종료 날짜가 미래이면 예외가 발생한다")
    void whenEndDateInFuture_thenThrowsException() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().plusDays(1);

        // when & then
        assertThatThrownBy(() -> service.execute(startDate, endDate))
                .isInstanceOf(InvalidStatisticsDateException.class);
    }

    @Test
    @DisplayName("조회 기간이 1년을 초과하면 예외가 발생한다")
    void whenDateRangeExceedsOneYear_thenThrowsException() {
        // given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 2); // 366일

        // when & then
        assertThatThrownBy(() -> service.execute(startDate, endDate))
                .isInstanceOf(DateRangeExceededException.class);
    }

    @Test
    @DisplayName("조회 기간이 정확히 1년이면 조회할 수 있다")
    void whenDateRangeExactlyOneYear_thenReturnsResults() {
        // given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 1); // 정확히 365일

        List<WithdrawalStatisticsDto> statistics = List.of(
                createStatistics(LocalDate.of(2023, 1, 1), 5L),
                createStatistics(LocalDate.of(2023, 6, 15), 3L),
                createStatistics(LocalDate.of(2024, 1, 1), 2L)
        );

        given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                .willReturn(statistics);

        // when
        List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("오늘을 포함한 기간을 조회할 수 있다")
    void whenEndDateIsToday_thenReturnsResults() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        List<WithdrawalStatisticsDto> statistics = List.of(
                createStatistics(startDate, 2L),
                createStatistics(endDate, 3L)
        );

        given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                .willReturn(statistics);

        // when
        List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("시작 날짜와 종료 날짜가 같으면 하루 통계를 조회할 수 있다")
    void whenStartAndEndDateAreSame_thenReturnsSingleDayStatistics() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);

        List<WithdrawalStatisticsDto> statistics = List.of(
                createStatistics(date, 10L)
        );

        given(withdrawalReasonQueryPort.aggregateDailyStatistics(date, date))
                .willReturn(statistics);

        // when
        List<WithdrawalStatisticsByDateResponse> result = service.execute(date, date);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).date()).isEqualTo(date);
        assertThat(result.get(0).count()).isEqualTo(10L);
    }

    @Test
    @DisplayName("기간 내 탈퇴가 없으면 빈 리스트를 반환한다")
    void whenNoWithdrawalsInRange_thenReturnsEmptyList() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 7);

        given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                .willReturn(List.of());

        // when
        List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

        // then
        assertThat(result).isEmpty();
    }
}
