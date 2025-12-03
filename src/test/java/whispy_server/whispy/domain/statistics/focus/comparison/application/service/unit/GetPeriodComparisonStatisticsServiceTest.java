package whispy_server.whispy.domain.statistics.focus.comparison.application.service.unit;
import whispy_server.whispy.domain.statistics.focus.comparison.application.service.GetPeriodComparisonStatisticsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.focus.comparison.application.port.out.QueryFocusComparisonPort;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * GetPeriodComparisonStatisticsService의 단위 테스트 클래스
 * <p>
 * 집중 기간 비교 서비스의 다양한 시나리오를 검증합니다.
 * 주간/월간/연간 비교 통계 계산을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetPeriodComparisonStatisticsService 테스트")
class GetPeriodComparisonStatisticsServiceTest {

    @InjectMocks
    private GetPeriodComparisonStatisticsService getPeriodComparisonStatisticsService;

    @Mock
    private QueryFocusComparisonPort queryFocusComparisonPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("모든 기간에 데이터가 없을 때 모든 값이 0이다")
    void whenNoDataInAllPeriods_thenAllValuesAreZero() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        PeriodComparisonResponse response = getPeriodComparisonStatisticsService.execute(
                FocusPeriodType.WEEK,
                testDate
        );

        // then
        assertThat(response.currentPeriodMinutes()).isEqualTo(0);
        assertThat(response.previousPeriodMinutes()).isEqualTo(0);
        assertThat(response.twoPeriodAgoMinutes()).isEqualTo(0);
        assertThat(response.differenceFromPrevious()).isEqualTo(0);
    }

    @Test
    @DisplayName("현재 기간이 이전 기간보다 많을 때 차이가 양수이다")
    void whenCurrentPeriodHasMoreThanPrevious_thenDifferenceIsPositive() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        // 현재 주: 600분
        FocusSessionDto currentSession = createSession(600);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                argThat(start -> start.equals(LocalDateTime.of(2024, 1, 15, 0, 0))),
                argThat(end -> end.equals(LocalDateTime.of(2024, 1, 21, 23, 59, 59)))
        )).willReturn(List.of(currentSession));

        // 이전 주: 300분
        FocusSessionDto previousSession = createSession(300);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                argThat(start -> start.equals(LocalDateTime.of(2024, 1, 8, 0, 0))),
                argThat(end -> end.equals(LocalDateTime.of(2024, 1, 14, 23, 59, 59)))
        )).willReturn(List.of(previousSession));

        // 2주 전: 200분
        FocusSessionDto twoWeeksAgoSession = createSession(200);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                argThat(start -> start.equals(LocalDateTime.of(2024, 1, 1, 0, 0))),
                argThat(end -> end.equals(LocalDateTime.of(2024, 1, 7, 23, 59, 59)))
        )).willReturn(List.of(twoWeeksAgoSession));

        // when
        PeriodComparisonResponse response = getPeriodComparisonStatisticsService.execute(
                FocusPeriodType.WEEK,
                testDate
        );

        // then
        assertThat(response.currentPeriodMinutes()).isEqualTo(600);
        assertThat(response.previousPeriodMinutes()).isEqualTo(300);
        assertThat(response.twoPeriodAgoMinutes()).isEqualTo(200);
        assertThat(response.differenceFromPrevious()).isEqualTo(300);
    }

    @Test
    @DisplayName("현재 기간이 이전 기간보다 적을 때 차이가 음수이다")
    void whenCurrentPeriodHasLessThanPrevious_thenDifferenceIsNegative() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);

        // 현재 주: 200분
        FocusSessionDto currentSession = createSession(200);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                argThat(start -> start.equals(LocalDateTime.of(2024, 1, 15, 0, 0))),
                argThat(end -> end.equals(LocalDateTime.of(2024, 1, 21, 23, 59, 59)))
        )).willReturn(List.of(currentSession));

        // 이전 주: 500분
        FocusSessionDto previousSession = createSession(500);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                argThat(start -> start.equals(LocalDateTime.of(2024, 1, 8, 0, 0))),
                argThat(end -> end.equals(LocalDateTime.of(2024, 1, 14, 23, 59, 59)))
        )).willReturn(List.of(previousSession));

        // 2주 전: 400분
        FocusSessionDto twoWeeksAgoSession = createSession(400);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                argThat(start -> start.equals(LocalDateTime.of(2024, 1, 1, 0, 0))),
                argThat(end -> end.equals(LocalDateTime.of(2024, 1, 7, 23, 59, 59)))
        )).willReturn(List.of(twoWeeksAgoSession));

        // when
        PeriodComparisonResponse response = getPeriodComparisonStatisticsService.execute(
                FocusPeriodType.WEEK,
                testDate
        );

        // then
        assertThat(response.currentPeriodMinutes()).isEqualTo(200);
        assertThat(response.previousPeriodMinutes()).isEqualTo(500);
        assertThat(response.twoPeriodAgoMinutes()).isEqualTo(400);
        assertThat(response.differenceFromPrevious()).isEqualTo(-300);
    }

    @Test
    @DisplayName("MONTH 기간의 비교가 정상 동작한다")
    void whenMonthPeriod_thenComparisonWorksCorrectly() {
        // given
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getPeriodComparisonStatisticsService.execute(FocusPeriodType.MONTH, testDate);

        // then
        verify(queryFocusComparisonPort).findByUserIdAndPeriod(
                eq(TEST_USER_ID),
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 1, 31, 23, 59, 59))
        );
    }

    @Test
    @DisplayName("YEAR 기간의 비교가 정상 동작한다")
    void whenYearPeriod_thenComparisonWorksCorrectly() {
        // given
        LocalDate testDate = LocalDate.of(2024, 6, 15);
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusComparisonPort.findByUserIdAndPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Collections.emptyList());

        // when
        getPeriodComparisonStatisticsService.execute(FocusPeriodType.YEAR, testDate);

        // then
        verify(queryFocusComparisonPort).findByUserIdAndPeriod(
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
        assertThatThrownBy(() -> getPeriodComparisonStatisticsService.execute(FocusPeriodType.WEEK, futureDate))
                .isInstanceOf(InvalidStatisticsDateException.class);
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
     * 테스트용 FocusSessionDto 객체를 생성합니다.
     *
     * @param durationMinutes 집중 지속 시간(분)
     * @return 생성된 FocusSessionDto 객체
     */
    private FocusSessionDto createSession(int durationMinutes) {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime endedAt = startedAt.plusMinutes(durationMinutes);
        return new FocusSessionDto(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationMinutes * 60,
                FocusTag.WORK,
                LocalDateTime.now()
        );
    }
}
