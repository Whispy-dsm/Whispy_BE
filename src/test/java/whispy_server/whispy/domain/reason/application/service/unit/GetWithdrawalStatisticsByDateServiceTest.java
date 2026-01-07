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
         * @param date  날짜
         * @param count 탈퇴 건수
         * @return 생성된 WithdrawalStatisticsDto 객체
         */
        private WithdrawalStatisticsDto createStatistics(LocalDate date, Integer count) {
                return new WithdrawalStatisticsDto(date, count);
        }

        @Test
        @DisplayName("기간 내 날짜별 탈퇴 통계를 조회할 수 있다")
        void whenQueryingStatistics_thenReturnsResults() {
                // given
                LocalDate startDate = LocalDate.of(2024, 1, 1);
                LocalDate endDate = LocalDate.of(2024, 1, 7);

                List<WithdrawalStatisticsDto> statistics = List.of(
                                createStatistics(LocalDate.of(2024, 1, 1), 3),
                                createStatistics(LocalDate.of(2024, 1, 3), 5),
                                createStatistics(LocalDate.of(2024, 1, 5), 2),
                                createStatistics(LocalDate.of(2024, 1, 7), 4));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                assertThat(result).hasSize(7); // 1월 1일 ~ 1월 7일 (7일)
                assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 1, 1));
                assertThat(result.get(0).count()).isEqualTo(3);
                assertThat(result.get(1).count()).isEqualTo(0); // 1월 2일 (데이터 없음)
                assertThat(result.get(2).count()).isEqualTo(5); // 1월 3일
                assertThat(result.get(6).count()).isEqualTo(4); // 1월 7일
        }

        @Test
        @DisplayName("탈퇴가 없는 날짜는 count 0으로 채워서 반환한다")
        void whenNoWithdrawalsOnSomeDates_thenFillsWithZero() {
                // given
                LocalDate startDate = LocalDate.of(2024, 1, 1);
                LocalDate endDate = LocalDate.of(2024, 1, 10);

                // 1월 1일과 1월 5일만 탈퇴가 있음
                List<WithdrawalStatisticsDto> statistics = List.of(
                                createStatistics(LocalDate.of(2024, 1, 1), 2),
                                createStatistics(LocalDate.of(2024, 1, 5), 3));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                assertThat(result).hasSize(10); // 1월 1일 ~ 1월 10일 (10일)
                assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 1, 1));
                assertThat(result.get(0).count()).isEqualTo(2);
                assertThat(result.get(1).count()).isEqualTo(0); // 1월 2일 (데이터 없음)
                assertThat(result.get(4).date()).isEqualTo(LocalDate.of(2024, 1, 5));
                assertThat(result.get(4).count()).isEqualTo(3);
                assertThat(result.get(9).count()).isEqualTo(0); // 1월 10일 (데이터 없음)
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
                                createStatistics(LocalDate.of(2023, 1, 1), 5),
                                createStatistics(LocalDate.of(2023, 6, 15), 3),
                                createStatistics(LocalDate.of(2024, 1, 1), 2));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                assertThat(result).hasSize(366); // 2023-01-01 ~ 2024-01-01 (366일, 2023년은 평년 365일 + 2024-01-01)
                assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2023, 1, 1));
                assertThat(result.get(0).count()).isEqualTo(5);
                assertThat(result.get(365).date()).isEqualTo(LocalDate.of(2024, 1, 1));
        }

        @Test
        @DisplayName("오늘을 포함한 기간을 조회할 수 있다")
        void whenEndDateIsToday_thenReturnsResults() {
                // given
                LocalDate startDate = LocalDate.now().minusDays(7);
                LocalDate endDate = LocalDate.now();

                List<WithdrawalStatisticsDto> statistics = List.of(
                                createStatistics(startDate, 2),
                                createStatistics(endDate, 3));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                assertThat(result).hasSize(8); // 7일 전 ~ 오늘 (8일)
                assertThat(result.get(0).date()).isEqualTo(startDate);
                assertThat(result.get(0).count()).isEqualTo(2);
                assertThat(result.get(7).date()).isEqualTo(endDate);
                assertThat(result.get(7).count()).isEqualTo(3);
        }

        @Test
        @DisplayName("시작 날짜와 종료 날짜가 같으면 하루 통계를 조회할 수 있다")
        void whenStartAndEndDateAreSame_thenReturnsSingleDayStatistics() {
                // given
                LocalDate date = LocalDate.of(2024, 1, 15);

                List<WithdrawalStatisticsDto> statistics = List.of(
                                createStatistics(date, 10));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(date, date))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(date, date);

                // then
                assertThat(result).hasSize(1);
                assertThat(result.get(0).date()).isEqualTo(date);
                assertThat(result.get(0).count()).isEqualTo(10);
        }

        @Test
        @DisplayName("기간 내 탈퇴가 없으면 모든 날짜를 count 0으로 반환한다")
        void whenNoWithdrawalsInRange_thenReturnsAllDatesWithZero() {
                // given
                LocalDate startDate = LocalDate.of(2024, 1, 1);
                LocalDate endDate = LocalDate.of(2024, 1, 7);

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(List.of());

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                assertThat(result).hasSize(7); // 1월 1일 ~ 1월 7일 (7일)
                assertThat(result).allMatch(stat -> stat.count() == 0); // 모두 0
        }

        @Test
        @DisplayName("[버그 수정 검증] 윤년을 포함한 정확히 1년(366일)은 조회할 수 있다")
        void whenLeapYearIncluded_thenCanQueryFullYear() {
                // given
                LocalDate startDate = LocalDate.of(2024, 1, 1); // 2024년은 윤년
                LocalDate endDate = LocalDate.of(2025, 1, 1); // 정확히 1년 후, 366일

                List<WithdrawalStatisticsDto> statistics = List.of(
                                createStatistics(LocalDate.of(2024, 1, 1), 5),
                                createStatistics(LocalDate.of(2025, 1, 1), 3));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                // 수정 전: ChronoUnit.DAYS(366일) > MAX_DAYS(365)로 예외 발생 (버그!)
                // 수정 후: ChronoUnit.YEARS(1년) && endDate == startDate.plusYears(1)로 정상 조회
                assertThat(result).hasSize(367); // 2024-01-01 ~ 2025-01-01 (367일, 윤년 366일 + 2025-01-01)
                assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 1, 1));
                assertThat(result.get(366).date()).isEqualTo(LocalDate.of(2025, 1, 1));
        }

        @Test
        @DisplayName("[버그 수정 검증] 윤년의 정확히 1년 후(2024-02-29 ~ 2025-02-28)는 조회할 수 있다")
        void whenExactlyOneYearFromLeapDay_thenReturnsResults() {
                // given
                LocalDate startDate = LocalDate.of(2024, 2, 29); // 윤년의 2월 29일
                LocalDate endDate = LocalDate.of(2025, 2, 28); // 정확히 1년 후 (2025년은 평년)

                List<WithdrawalStatisticsDto> statistics = List.of(
                                createStatistics(startDate, 2),
                                createStatistics(endDate, 3));

                given(withdrawalReasonQueryPort.aggregateDailyStatistics(startDate, endDate))
                                .willReturn(statistics);

                // when
                List<WithdrawalStatisticsByDateResponse> result = service.execute(startDate, endDate);

                // then
                // 2024-02-29.plusYears(1) = 2025-02-28 (정확히 1년)
                // ChronoUnit.YEARS.between() = 0년이지만, 실제로는 startDate.plusYears(1)과 같으므로 통과
                assertThat(result).isNotEmpty();
                assertThat(result.get(0).date()).isEqualTo(startDate);
        }

        @Test
        @DisplayName("[버그 수정 검증] 윤년의 1년 + 1일은 예외가 발생한다")
        void whenLeapYearPlusOneDay_thenThrowsException() {
                // given
                LocalDate startDate = LocalDate.of(2024, 2, 29); // 윤년의 2월 29일
                LocalDate endDate = LocalDate.of(2025, 3, 1); // 1년 + 1일

                // when & then
                // endDate > startDate.plusYears(1) (2025-02-28)이므로 예외 발생
                assertThatThrownBy(() -> service.execute(startDate, endDate))
                                .isInstanceOf(DateRangeExceededException.class);
        }
}
