package whispy_server.whispy.domain.statistics.sleep.summary.application.service.unit;
import whispy_server.whispy.domain.statistics.sleep.summary.application.service.GetSleepStatisticsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepDetailedAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.out.QuerySleepStatisticsPort;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * GetSleepStatisticsService의 단위 테스트 클래스
 * <p>
 * 수면 통계 조회 서비스의 다양한 시나리오를 검증합니다.
 * 일관성 점수 계산, 기간 범위 계산, 경계값 처리 등을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetSleepStatisticsService 테스트")
class GetSleepStatisticsServiceTest {

    @InjectMocks
    private GetSleepStatisticsService getSleepStatisticsService;

    @Mock
    private QuerySleepStatisticsPort querySleepStatisticsPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("세션이 0개일 때 일관성 점수는 100점이다")
    void whenNoSessions_thenConsistencyScoreIs100() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        SleepStatisticsResponse response = getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate);

        // then
        assertThat(response.sleepConsistency()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("세션이 1개일 때 일관성 점수는 100점이다")
    void whenOneSessions_thenConsistencyScoreIs100() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        LocalDateTime session1Start = LocalDateTime.of(2024, 1, 15, 23, 0);
        SleepSessionDto session = createSession(session1Start, 480);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(1, 480, 480, 1380, 420);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(480);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(List.of(session));

        // when
        SleepStatisticsResponse response = getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate);

        // then
        assertThat(response.sleepConsistency()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("세션이 2개일 때 일관성 점수가 정상 계산된다")
    void whenTwoSessions_thenConsistencyScoreIsCalculated() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        LocalDateTime session1Start = LocalDateTime.of(2024, 1, 14, 23, 0);
        LocalDateTime session2Start = LocalDateTime.of(2024, 1, 15, 23, 30);
        
        List<SleepSessionDto> sessions = List.of(
                createSession(session1Start, 480),
                createSession(session2Start, 480)
        );
        
        int avgBedTimeMinutes = (23 * 60 + 0 + 23 * 60 + 30) / 2;
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(2, 960, 480, avgBedTimeMinutes, 420);
        
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(480);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessions);

        // when
        SleepStatisticsResponse response = getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate);

        // then
        assertThat(response.sleepConsistency()).isLessThan(100.0);
        assertThat(response.sleepConsistency()).isGreaterThanOrEqualTo(0.0);
    }

    @Test
    @DisplayName("다수의 세션이 있을 때 일관성 점수가 정상 계산된다")
    void whenMultipleSessions_thenConsistencyScoreIsCalculated() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        List<SleepSessionDto> sessions = List.of(
                createSession(LocalDateTime.of(2024, 1, 10, 23, 0), 480),
                createSession(LocalDateTime.of(2024, 1, 11, 23, 15), 480),
                createSession(LocalDateTime.of(2024, 1, 12, 23, 30), 480),
                createSession(LocalDateTime.of(2024, 1, 13, 23, 0), 480),
                createSession(LocalDateTime.of(2024, 1, 14, 23, 45), 480)
        );
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(5, 2400, 480, 1398, 420);
        
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(480);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessions);

        // when
        SleepStatisticsResponse response = getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate);

        // then
        assertThat(response.sleepConsistency()).isLessThan(100.0);
        assertThat(response.sleepConsistency()).isGreaterThanOrEqualTo(0.0);
    }

    @ParameterizedTest
    @MethodSource("provideMidnightCrossingCases")
    @DisplayName("자정을 넘는 취침 시간에서 분 계산이 정확히 수행된다")
    void whenBedTimesCrossMidnight_thenMinutesAreCalculatedCorrectly(
            LocalDateTime bedTime1, 
            LocalDateTime bedTime2, 
            int expectedAvgMinutes
    ) {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        List<SleepSessionDto> sessions = List.of(
                createSession(bedTime1, 480),
                createSession(bedTime2, 480)
        );
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(2, 960, 480, expectedAvgMinutes, 420);
        
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(480);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessions);

        // when
        SleepStatisticsResponse response = getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate);

        // then
        assertThat(response.sleepConsistency()).isLessThanOrEqualTo(100.0);
        assertThat(response.sleepConsistency()).isGreaterThanOrEqualTo(0.0);
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryTimeValues")
    @DisplayName("평균 취침 분의 경계값에서 convertMinutesToLocalTime이 정상 동작한다")
    void whenAverageBedTimeMinutesAtBoundary_thenConvertMinutesToLocalTimeWorksCorrectly(
            int avgMinutes,
            LocalTime expectedTime
    ) {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepSessionDto session = createSession(LocalDateTime.of(2024, 1, 15, 0, 0), 480);
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(1, 480, 480, avgMinutes, avgMinutes);
        
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(480);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(List.of(session));

        // when
        SleepStatisticsResponse response = getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate);

        // then
        assertThat(response.averageBedTime()).isEqualTo(expectedTime);
        assertThat(response.averageWakeTime()).isEqualTo(expectedTime);
    }

    @Test
    @DisplayName("평균 취침 분이 1440 이상일 때 예외가 발생한다")
    void whenAverageBedTimeMinutesIsOver1440_thenThrowsException() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepSessionDto session = createSession(LocalDateTime.of(2024, 1, 15, 0, 0), 480);
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(1, 480, 480, 1440, 420);
        
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(480);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(List.of(session));

        // when & then
        assertThatThrownBy(() -> getSleepStatisticsService.execute(SleepPeriodType.WEEK, testDate))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("WEEK 기간의 calculatePeriodRange가 월요일부터 일요일까지 반환한다")
    void whenPeriodIsWeek_thenCalculatePeriodRangeReturnsMondayToSunday() {
        // given
        LocalDate wednesday = LocalDate.of(2024, 1, 17);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, wednesday)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.WEEK, wednesday);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 15, 0, 0)),
                eq(LocalDateTime.of(2024, 1, 21, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("MONTH 기간의 calculatePeriodRange가 월 초부터 월 말까지 반환한다")
    void whenPeriodIsMonth_thenCalculatePeriodRangeReturnsFirstToLastDayOfMonth() {
        // given
        LocalDate midMonth = LocalDate.of(2024, 1, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, midMonth)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.MONTH, midMonth);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 1, 31, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("2월의 MONTH 기간 계산이 정확히 수행된다")
    void whenMonthIsFebruaryNonLeapYear_thenCalculatePeriodRangeReturnsCorrectDays() {
        // given
        LocalDate february = LocalDate.of(2023, 2, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, february)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.MONTH, february);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2023, 2, 1, 0, 0)),
                eq(LocalDateTime.of(2023, 2, 28, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("윤년 2월의 MONTH 기간 계산이 정확히 수행된다")
    void whenMonthIsFebruaryLeapYear_thenCalculatePeriodRangeReturnsCorrectDays() {
        // given
        LocalDate leapFebruary = LocalDate.of(2024, 2, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, leapFebruary)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.MONTH, leapFebruary);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 2, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 2, 29, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("YEAR 기간의 calculatePeriodRange가 년 초부터 년 말까지 반환한다")
    void whenPeriodIsYear_thenCalculatePeriodRangeReturnsFirstToLastDayOfYear() {
        // given
        LocalDate midYear = LocalDate.of(2024, 6, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, midYear)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.YEAR, midYear);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("평년의 YEAR 기간이 365일로 계산된다")
    void whenYearIsNonLeapYear_thenCalculatePeriodRangeReturns365Days() {
        // given
        LocalDate nonLeapYear = LocalDate.of(2023, 6, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, nonLeapYear)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.YEAR, nonLeapYear);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2023, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2023, 12, 31, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("윤년의 YEAR 기간이 366일로 계산된다")
    void whenYearIsLeapYear_thenCalculatePeriodRangeReturns366Days() {
        // given
        LocalDate leapYear = LocalDate.of(2024, 6, 15);
        User user = createUser();
        
        given(userFacadeUseCase.currentUser()).willReturn(user);
        
        SleepDetailedAggregationDto aggregation = new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
        given(querySleepStatisticsPort.aggregateDetailedStatistics(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(querySleepStatisticsPort.sumMinutesByDate(TEST_USER_ID, leapYear)).willReturn(0);
        given(querySleepStatisticsPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getSleepStatisticsService.execute(SleepPeriodType.YEAR, leapYear);

        // then
        verify(querySleepStatisticsPort).aggregateDetailedStatistics(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
        );
    }

    /**
     * 자정을 넘는 취침 시간 테스트 케이스를 제공합니다.
     * <p>
     * 취침 시간이 자정을 넘어갈 때 분 계산이 정확히 수행되는지 검증하기 위한 테스트 데이터입니다.
     * </p>
     *
     * @return 취침 시간1, 취침 시간2, 예상 평균 분을 포함하는 Arguments 스트림
     */
    private static Stream<Arguments> provideMidnightCrossingCases() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2024, 1, 14, 23, 30),
                        LocalDateTime.of(2024, 1, 15, 0, 10),
                        (23 * 60 + 30 + 0 * 60 + 10) / 2
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 1, 14, 23, 45),
                        LocalDateTime.of(2024, 1, 15, 0, 15),
                        (23 * 60 + 45 + 0 * 60 + 15) / 2
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 1, 14, 23, 0),
                        LocalDateTime.of(2024, 1, 15, 1, 0),
                        (23 * 60 + 0 + 1 * 60 + 0) / 2
                )
        );
    }

    /**
     * 경계값 시간 테스트 케이스를 제공합니다.
     * <p>
     * 분 단위로 표현된 시간을 LocalTime으로 변환할 때 경계값에서 정상 동작하는지 검증하기 위한 테스트 데이터입니다.
     * </p>
     *
     * @return 평균 분과 예상 LocalTime을 포함하는 Arguments 스트림
     */
    private static Stream<Arguments> provideBoundaryTimeValues() {
        return Stream.of(
                Arguments.of(0, LocalTime.of(0, 0)),
                Arguments.of(1, LocalTime.of(0, 1)),
                Arguments.of(1439, LocalTime.of(23, 59))
        );
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

    /**
     * 테스트용 SleepSessionDto 객체를 생성합니다.
     *
     * @param startedAt 수면 시작 시간
     * @param durationMinutes 수면 지속 시간(분)
     * @return 생성된 SleepSessionDto 객체
     */
    private SleepSessionDto createSession(LocalDateTime startedAt, int durationMinutes) {
        LocalDateTime endedAt = startedAt.plusMinutes(durationMinutes);
        return new SleepSessionDto(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationMinutes * 60,
                LocalDateTime.now()
        );
    }
}
