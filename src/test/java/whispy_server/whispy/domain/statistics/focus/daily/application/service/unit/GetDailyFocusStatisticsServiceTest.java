package whispy_server.whispy.domain.statistics.focus.daily.application.service.unit;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.application.service.GetDailyFocusStatisticsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
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
 * GetDailyFocusStatisticsService의 단위 테스트 클래스
 *
 * 일별 집중 통계 조회 서비스의 다양한 시나리오를 검증합니다.
 * 시간별, 일별, 월별 통계 생성 및 태그별 데이터 집계를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetDailyFocusStatisticsService 테스트")
class GetDailyFocusStatisticsServiceTest {

    @InjectMocks
    private GetDailyFocusStatisticsService getDailyFocusStatisticsService;

    @Mock
    private QueryFocusStatisticsPort queryFocusStatisticsPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("TODAY 기간에 데이터가 없을 때 24시간 모두 0분으로 생성된다")
    void whenTodayPeriodWithNoData_then24HoursWithZeroMinutes() {
        // given
        LocalDate today = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusStatisticsPort.aggregateHourlyMinutes(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());
        given(queryFocusStatisticsPort.aggregateHourlyByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        DailyFocusStatisticsResponse response = getDailyFocusStatisticsService.execute(
                FocusPeriodType.TODAY,
                today
        );

        // then
        assertThat(response.hourlyData()).hasSize(24);
        assertThat(response.hourlyData()).allMatch(hour ->
                hour.minutes() == 0 &&
                hour.tagData().size() == FocusTag.values().length
        );
        assertThat(response.dailyData()).isNull();
        assertThat(response.monthlyData()).isNull();
    }

    @Test
    @DisplayName("TODAY 기간에 특정 시간에 데이터가 있을 때 해당 시간에만 분이 설정된다")
    void whenTodayPeriodWithDataAtSpecificHour_thenOnlyThatHourHasMinutes() {
        // given
        LocalDate today = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        List<HourlyFocusAggregationDto> hourlyAggregations = List.of(
                new HourlyFocusAggregationDto(9, 60),
                new HourlyFocusAggregationDto(14, 90)
        );

        List<HourlyTagFocusAggregationDto> tagAggregations = List.of(
                new HourlyTagFocusAggregationDto(9, FocusTag.WORK, 60),
                new HourlyTagFocusAggregationDto(14, FocusTag.STUDY, 90)
        );

        given(queryFocusStatisticsPort.aggregateHourlyMinutes(eq(TEST_USER_ID), any(), any()))
                .willReturn(hourlyAggregations);
        given(queryFocusStatisticsPort.aggregateHourlyByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(tagAggregations);

        // when
        DailyFocusStatisticsResponse response = getDailyFocusStatisticsService.execute(
                FocusPeriodType.TODAY,
                today
        );

        // then
        assertThat(response.hourlyData()).hasSize(24);
        assertThat(response.hourlyData().stream()
                .filter(h -> h.hour() == 9)
                .findFirst()
                .get()
                .minutes()).isEqualTo(60);
        assertThat(response.hourlyData().stream()
                .filter(h -> h.hour() == 14)
                .findFirst()
                .get()
                .minutes()).isEqualTo(90);
    }

    @Test
    @DisplayName("WEEK 기간에 데이터가 없을 때 7일 모두 0분으로 생성된다")
    void whenWeekPeriodWithNoData_then7DaysWithZeroMinutes() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 17);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusStatisticsPort.aggregateDailyMinutes(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());
        given(queryFocusStatisticsPort.aggregateDailyByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        DailyFocusStatisticsResponse response = getDailyFocusStatisticsService.execute(
                FocusPeriodType.WEEK,
                date
        );

        // then
        assertThat(response.dailyData()).hasSize(7);
        assertThat(response.dailyData()).allMatch(day ->
                day.minutes() == 0 &&
                day.tagData().size() == FocusTag.values().length
        );
        assertThat(response.hourlyData()).isNull();
        assertThat(response.monthlyData()).isNull();
    }

    @Test
    @DisplayName("YEAR 기간에 데이터가 없을 때 12개월 모두 0분으로 생성된다")
    void whenYearPeriodWithNoData_then12MonthsWithZeroMinutes() {
        // given
        LocalDate date = LocalDate.of(2024, 6, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusStatisticsPort.aggregateMonthlyMinutes(TEST_USER_ID, 2024))
                .willReturn(Collections.emptyList());
        given(queryFocusStatisticsPort.aggregateMonthlyByTag(TEST_USER_ID, 2024))
                .willReturn(Collections.emptyList());

        // when
        DailyFocusStatisticsResponse response = getDailyFocusStatisticsService.execute(
                FocusPeriodType.YEAR,
                date
        );

        // then
        assertThat(response.monthlyData()).hasSize(12);
        assertThat(response.monthlyData()).allMatch(month ->
                month.minutes() == 0 &&
                month.tagData().size() == FocusTag.values().length
        );
        assertThat(response.hourlyData()).isNull();
        assertThat(response.dailyData()).isNull();
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
