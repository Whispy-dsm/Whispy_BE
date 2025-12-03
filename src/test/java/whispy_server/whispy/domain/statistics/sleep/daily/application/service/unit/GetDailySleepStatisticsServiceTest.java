package whispy_server.whispy.domain.statistics.sleep.daily.application.service.unit;
import whispy_server.whispy.domain.statistics.sleep.daily.application.service.GetDailySleepStatisticsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response.DailySleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.DailySleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.MonthlySleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.daily.application.port.out.QuerySleepStatisticsPort;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * GetDailySleepStatisticsService의 단위 테스트 클래스
 * <p>
 * 일별 수면 통계 조회 서비스의 다양한 시나리오를 검증합니다.
 * 일별, 월별 통계 생성을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetDailySleepStatisticsService 테스트")
class GetDailySleepStatisticsServiceTest {

    @InjectMocks
    private GetDailySleepStatisticsService getDailySleepStatisticsService;

    @Mock
    private QuerySleepStatisticsPort querySleepStatisticsPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("WEEK 기간에 데이터가 없을 때 7일 모두 0분으로 생성된다")
    void whenWeekPeriodWithNoData_then7DaysWithZeroMinutes() {
        // given
        LocalDate wednesday = LocalDate.of(2024, 1, 17);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepStatisticsPort.aggregateDailyMinutes(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        DailySleepStatisticsResponse response = getDailySleepStatisticsService.execute(
                SleepPeriodType.WEEK,
                wednesday
        );

        // then
        assertThat(response.dailyData()).hasSize(7);
        assertThat(response.dailyData()).allMatch(day -> day.minutes() == 0);
        assertThat(response.monthlyData()).isNull();
    }

    @Test
    @DisplayName("WEEK 기간에 특정 날짜에 데이터가 있을 때 해당 날짜에만 분이 설정된다")
    void whenWeekPeriodWithDataAtSpecificDays_thenOnlyThoseDaysHaveMinutes() {
        // given
        LocalDate wednesday = LocalDate.of(2024, 1, 17);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        LocalDate monday = LocalDate.of(2024, 1, 15);
        LocalDate friday = LocalDate.of(2024, 1, 19);

        List<DailySleepAggregationDto> aggregations = List.of(
                new DailySleepAggregationDto(monday, 480),
                new DailySleepAggregationDto(friday, 420)
        );

        given(querySleepStatisticsPort.aggregateDailyMinutes(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregations);

        // when
        DailySleepStatisticsResponse response = getDailySleepStatisticsService.execute(
                SleepPeriodType.WEEK,
                wednesday
        );

        // then
        assertThat(response.dailyData()).hasSize(7);
        assertThat(response.dailyData().stream()
                .filter(d -> d.date().equals(monday))
                .findFirst()
                .get()
                .minutes()).isEqualTo(480);
        assertThat(response.dailyData().stream()
                .filter(d -> d.date().equals(friday))
                .findFirst()
                .get()
                .minutes()).isEqualTo(420);
    }

    @Test
    @DisplayName("MONTH 기간에 데이터가 없을 때 해당 월의 모든 날이 0분으로 생성된다")
    void whenMonthPeriodWithNoData_thenAllDaysInMonthWithZeroMinutes() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepStatisticsPort.aggregateDailyMinutes(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        DailySleepStatisticsResponse response = getDailySleepStatisticsService.execute(
                SleepPeriodType.MONTH,
                date
        );

        // then
        assertThat(response.dailyData()).hasSize(31);
        assertThat(response.dailyData()).allMatch(day -> day.minutes() == 0);
    }

    @Test
    @DisplayName("YEAR 기간에 데이터가 없을 때 12개월 모두 0분으로 생성된다")
    void whenYearPeriodWithNoData_then12MonthsWithZeroMinutes() {
        // given
        LocalDate date = LocalDate.of(2024, 6, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepStatisticsPort.aggregateMonthlyMinutes(TEST_USER_ID, 2024))
                .willReturn(Collections.emptyList());

        // when
        DailySleepStatisticsResponse response = getDailySleepStatisticsService.execute(
                SleepPeriodType.YEAR,
                date
        );

        // then
        assertThat(response.monthlyData()).hasSize(12);
        assertThat(response.monthlyData()).allMatch(month -> month.minutes() == 0);
        assertThat(response.dailyData()).isNull();
    }

    @Test
    @DisplayName("YEAR 기간에 특정 월에 데이터가 있을 때 해당 월에만 분이 설정된다")
    void whenYearPeriodWithDataAtSpecificMonths_thenOnlyThoseMonthsHaveMinutes() {
        // given
        LocalDate date = LocalDate.of(2024, 6, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        List<MonthlySleepAggregationDto> aggregations = List.of(
                new MonthlySleepAggregationDto(1, 3600),
                new MonthlySleepAggregationDto(6, 4200),
                new MonthlySleepAggregationDto(12, 3900)
        );

        given(querySleepStatisticsPort.aggregateMonthlyMinutes(TEST_USER_ID, 2024))
                .willReturn(aggregations);

        // when
        DailySleepStatisticsResponse response = getDailySleepStatisticsService.execute(
                SleepPeriodType.YEAR,
                date
        );

        // then
        assertThat(response.monthlyData()).hasSize(12);
        assertThat(response.monthlyData().stream()
                .filter(m -> m.month() == 1)
                .findFirst()
                .get()
                .minutes()).isEqualTo(3600);
        assertThat(response.monthlyData().stream()
                .filter(m -> m.month() == 6)
                .findFirst()
                .get()
                .minutes()).isEqualTo(4200);
        assertThat(response.monthlyData().stream()
                .filter(m -> m.month() == 12)
                .findFirst()
                .get()
                .minutes()).isEqualTo(3900);
    }

    /**
     * 테스트용 User 객체를 생성합니다.
     *
     * @return 기본 정보가 설정된 User 객체
     */
    private User createUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                "password",
                "TestUser",
                null,
                Gender.MALE,
                Role.USER,
                null,
                null,
                LocalDateTime.now()
        );
    }
}
