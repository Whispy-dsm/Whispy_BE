package whispy_server.whispy.domain.statistics.focus.summary.application.service.unit;
import whispy_server.whispy.domain.statistics.focus.summary.application.service.GetFocusStatisticsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * GetFocusStatisticsService의 단위 테스트 클래스
 *
 * 집중 통계 조회 서비스의 다양한 시나리오를 검증합니다.
 * 태그별 집중 시간 집계, 기간별 통계 계산 등을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetFocusStatisticsService 테스트")
class GetFocusStatisticsServiceTest {

    @InjectMocks
    private GetFocusStatisticsService getFocusStatisticsService;

    @Mock
    private QueryFocusStatisticsPort queryFocusStatisticsPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("세션이 0개일 때 모든 통계가 0이다")
    void whenNoSessions_thenAllStatisticsAreZero() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(0, 0);
        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(0);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(0);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        FocusStatisticsResponse response = getFocusStatisticsService.execute(FocusPeriodType.WEEK, testDate);

        // then
        assertThat(response.totalCount()).isEqualTo(0);
        assertThat(response.totalMinutes()).isEqualTo(0);
        assertThat(response.todayMinutes()).isEqualTo(0);
        assertThat(response.totalDays()).isEqualTo(0);
        assertThat(response.tagMinutes()).hasSize(FocusTag.values().length);
        assertThat(response.tagMinutes().values()).allMatch(minutes -> minutes == 0);
    }

    @Test
    @DisplayName("단일 태그로 세션이 있을 때 통계가 정상 계산된다")
    void whenSingleTagSessions_thenStatisticsAreCalculatedCorrectly() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(3, 180);
        List<TagMinutesDto> tagAggregations = List.of(
                new TagMinutesDto(FocusTag.WORK, 180)
        );

        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(60);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(2);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(tagAggregations);

        // when
        FocusStatisticsResponse response = getFocusStatisticsService.execute(FocusPeriodType.WEEK, testDate);

        // then
        assertThat(response.totalCount()).isEqualTo(3);
        assertThat(response.totalMinutes()).isEqualTo(180);
        assertThat(response.todayMinutes()).isEqualTo(60);
        assertThat(response.totalDays()).isEqualTo(2);
        assertThat(response.tagMinutes().get(FocusTag.WORK)).isEqualTo(180);
        assertThat(response.tagMinutes().get(FocusTag.STUDY)).isEqualTo(0);
    }

    @Test
    @DisplayName("여러 태그로 세션이 있을 때 각 태그별 시간이 정상 집계된다")
    void whenMultipleTagSessions_thenEachTagMinutesAreAggregatedCorrectly() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(5, 360);
        List<TagMinutesDto> tagAggregations = List.of(
                new TagMinutesDto(FocusTag.WORK, 120),
                new TagMinutesDto(FocusTag.STUDY, 180),
                new TagMinutesDto(FocusTag.READ, 60)
        );

        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(90);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(3);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(tagAggregations);

        // when
        FocusStatisticsResponse response = getFocusStatisticsService.execute(FocusPeriodType.WEEK, testDate);

        // then
        assertThat(response.totalCount()).isEqualTo(5);
        assertThat(response.totalMinutes()).isEqualTo(360);
        assertThat(response.todayMinutes()).isEqualTo(90);
        assertThat(response.tagMinutes().get(FocusTag.WORK)).isEqualTo(120);
        assertThat(response.tagMinutes().get(FocusTag.STUDY)).isEqualTo(180);
        assertThat(response.tagMinutes().get(FocusTag.READ)).isEqualTo(60);
        assertThat(response.tagMinutes().get(FocusTag.MEDITATION)).isEqualTo(0);
        assertThat(response.tagMinutes().get(FocusTag.SPORT)).isEqualTo(0);
    }

    @Test
    @DisplayName("모든 태그에 세션이 있을 때 모든 태그 시간이 집계된다")
    void whenAllTagsHaveSessions_thenAllTagMinutesAreAggregated() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(10, 600);
        List<TagMinutesDto> tagAggregations = List.of(
                new TagMinutesDto(FocusTag.WORK, 150),
                new TagMinutesDto(FocusTag.STUDY, 200),
                new TagMinutesDto(FocusTag.READ, 100),
                new TagMinutesDto(FocusTag.MEDITATION, 80),
                new TagMinutesDto(FocusTag.SPORT, 70)
        );

        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, testDate)).willReturn(120);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(5);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(tagAggregations);

        // when
        FocusStatisticsResponse response = getFocusStatisticsService.execute(FocusPeriodType.WEEK, testDate);

        // then
        assertThat(response.totalCount()).isEqualTo(10);
        assertThat(response.totalMinutes()).isEqualTo(600);
        assertThat(response.tagMinutes()).hasSize(FocusTag.values().length);
        assertThat(response.tagMinutes().get(FocusTag.WORK)).isEqualTo(150);
        assertThat(response.tagMinutes().get(FocusTag.STUDY)).isEqualTo(200);
        assertThat(response.tagMinutes().get(FocusTag.READ)).isEqualTo(100);
        assertThat(response.tagMinutes().get(FocusTag.MEDITATION)).isEqualTo(80);
        assertThat(response.tagMinutes().get(FocusTag.SPORT)).isEqualTo(70);
    }

    @ParameterizedTest
    @MethodSource("providePeriodTypes")
    @DisplayName("다양한 기간 타입에서 통계 조회가 정상 동작한다")
    void whenDifferentPeriodTypes_thenStatisticsAreRetrievedCorrectly(
            FocusPeriodType periodType,
            LocalDate date
    ) {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(2, 120);
        List<TagMinutesDto> tagAggregations = List.of(
                new TagMinutesDto(FocusTag.WORK, 120)
        );

        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, date)).willReturn(60);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(1);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(tagAggregations);

        // when
        FocusStatisticsResponse response = getFocusStatisticsService.execute(periodType, date);

        // then
        assertThat(response.totalCount()).isEqualTo(2);
        assertThat(response.totalMinutes()).isEqualTo(120);
        assertThat(response.totalDays()).isEqualTo(1);
        verify(queryFocusStatisticsPort).aggregateByPeriod(eq(TEST_USER_ID), any(), any());
    }

    @Test
    @DisplayName("WEEK 기간의 통계가 월요일부터 일요일까지 조회된다")
    void whenPeriodIsWeek_thenStatisticsAreFromMondayToSunday() {
        // given
        LocalDate wednesday = LocalDate.of(2024, 1, 17);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(0, 0);
        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, wednesday)).willReturn(0);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(0);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getFocusStatisticsService.execute(FocusPeriodType.WEEK, wednesday);

        // then
        verify(queryFocusStatisticsPort).aggregateByPeriod(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 15, 0, 0)),
                eq(LocalDateTime.of(2024, 1, 21, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("MONTH 기간의 통계가 월 초부터 월 말까지 조회된다")
    void whenPeriodIsMonth_thenStatisticsAreFromFirstToLastDayOfMonth() {
        // given
        LocalDate midMonth = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(0, 0);
        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, midMonth)).willReturn(0);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(0);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getFocusStatisticsService.execute(FocusPeriodType.MONTH, midMonth);

        // then
        verify(queryFocusStatisticsPort).aggregateByPeriod(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 1, 31, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("YEAR 기간의 통계가 년 초부터 년 말까지 조회된다")
    void whenPeriodIsYear_thenStatisticsAreFromFirstToLastDayOfYear() {
        // given
        LocalDate midYear = LocalDate.of(2024, 6, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        FocusAggregationDto aggregation = new FocusAggregationDto(0, 0);
        given(queryFocusStatisticsPort.aggregateByPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(aggregation);
        given(queryFocusStatisticsPort.sumMinutesByDate(TEST_USER_ID, midYear)).willReturn(0);
        given(queryFocusStatisticsPort.countDistinctDays(eq(TEST_USER_ID), any(), any())).willReturn(0);
        given(queryFocusStatisticsPort.aggregateByTag(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getFocusStatisticsService.execute(FocusPeriodType.YEAR, midYear);

        // then
        verify(queryFocusStatisticsPort).aggregateByPeriod(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("미래 날짜로 조회 시 예외가 발생한다")
    void whenDateIsInFuture_thenThrowsException() {
        // given
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // when & then
        assertThatThrownBy(() -> getFocusStatisticsService.execute(FocusPeriodType.WEEK, futureDate))
                .isInstanceOf(InvalidStatisticsDateException.class);
    }

    /**
     * 다양한 기간 타입 테스트 케이스를 제공합니다.
     *
     * @return 기간 타입과 날짜를 포함하는 Arguments 스트림
     */
    private static Stream<Arguments> providePeriodTypes() {
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        return Stream.of(
                Arguments.of(FocusPeriodType.TODAY, testDate),
                Arguments.of(FocusPeriodType.WEEK, testDate),
                Arguments.of(FocusPeriodType.MONTH, testDate),
                Arguments.of(FocusPeriodType.YEAR, testDate)
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
}
