package whispy_server.whispy.domain.statistics.activity.applicatoin.service.unit;
import whispy_server.whispy.domain.statistics.activity.applicatoin.service.GetWeeklyActivityService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklyActivityResponse;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.QueryActivityMinutesPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

/**
 * GetWeeklyActivityService의 단위 테스트 클래스
 *
 * 주간 활동 통계 조회 서비스의 다양한 시나리오를 검증합니다.
 * 20주간의 활동 데이터 집계, 월 표시자 생성 등을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetWeeklyActivityService 테스트")
class GetWeeklyActivityServiceTest {

    @InjectMocks
    private GetWeeklyActivityService getWeeklyActivityService;

    @Mock
    private QueryActivityMinutesPort queryActivityMinutesPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("활동 데이터가 없을 때 빈 분 데이터로 20주가 생성된다")
    void whenNoActivityData_then20WeeksAreCreatedWithZeroMinutes() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Map.of());

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.weeks()).hasSize(20);
        assertThat(response.weeks().get(0).days()).hasSize(7);
        assertThat(response.weeks().get(0).days()).allMatch(day -> day.totalMinutes() == 0);
    }

    @Test
    @DisplayName("단일 날짜에 활동 데이터가 있을 때 해당 날짜에만 분이 설정된다")
    void whenSingleDayHasActivity_thenOnlyThatDayHasMinutes() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        LocalDate today = LocalDate.now();
        Map<LocalDate, Integer> sessionMinutes = new HashMap<>();
        sessionMinutes.put(today, 120);

        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessionMinutes);

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.weeks()).hasSize(20);
        boolean foundActivity = response.weeks().stream()
                .flatMap(week -> week.days().stream())
                .anyMatch(day -> day.date().equals(today) && day.totalMinutes() == 120);
        assertThat(foundActivity).isTrue();
    }

    @Test
    @DisplayName("여러 날짜에 활동 데이터가 있을 때 각 날짜에 분이 정상 설정된다")
    void whenMultipleDaysHaveActivity_thenEachDayHasCorrectMinutes() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        LocalDate today = LocalDate.now();
        Map<LocalDate, Integer> sessionMinutes = new HashMap<>();
        sessionMinutes.put(today, 120);
        sessionMinutes.put(today.minusDays(1), 90);
        sessionMinutes.put(today.minusDays(2), 60);

        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessionMinutes);

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.weeks()).hasSize(20);

        long daysWithActivity = response.weeks().stream()
                .flatMap(week -> week.days().stream())
                .filter(day -> day.totalMinutes() > 0)
                .count();

        assertThat(daysWithActivity).isEqualTo(3);
    }

    @Test
    @DisplayName("한 주의 모든 날에 활동이 있을 때 해당 주의 모든 날에 분이 설정된다")
    void whenFullWeekHasActivity_thenAllDaysInWeekHaveMinutes() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        LocalDate today = LocalDate.now();
        Map<LocalDate, Integer> sessionMinutes = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            sessionMinutes.put(today.minusDays(i), 60 + (i * 10));
        }

        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessionMinutes);

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        long daysWithActivity = response.weeks().stream()
                .flatMap(week -> week.days().stream())
                .filter(day -> day.totalMinutes() > 0)
                .count();

        assertThat(daysWithActivity).isEqualTo(7);
    }

    @Test
    @DisplayName("20주 전체에 활동이 있을 때 모든 데이터가 정상 집계된다")
    void whenFull20WeeksHaveActivity_thenAllDataIsAggregatedCorrectly() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        LocalDate today = LocalDate.now();
        Map<LocalDate, Integer> sessionMinutes = new HashMap<>();

        for (int i = 0; i < 140; i++) {
            sessionMinutes.put(today.minusDays(i), 30 + (i % 60));
        }

        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessionMinutes);

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.weeks()).hasSize(20);

        long totalDaysWithActivity = response.weeks().stream()
                .flatMap(week -> week.days().stream())
                .filter(day -> day.totalMinutes() > 0)
                .count();

        assertThat(totalDaysWithActivity).isEqualTo(140);
    }

    @Test
    @DisplayName("응답의 시작 날짜가 오늘로부터 139일 전이다")
    void whenExecuted_thenStartDateIs139DaysAgo() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Map.of());

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        LocalDate expectedStartDate = LocalDate.now().minusDays(139);
        assertThat(response.startDate()).isEqualTo(expectedStartDate);
    }

    @Test
    @DisplayName("응답의 종료 날짜가 오늘이다")
    void whenExecuted_thenEndDateIsToday() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Map.of());

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.endDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("각 주의 날짜 데이터에 요일 정보가 포함된다")
    void whenExecuted_thenEachDayHasDayOfWeekInfo() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Map.of());

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.weeks().get(0).days()).allMatch(day ->
                day.dayOfWeek() != null &&
                day.date().getDayOfWeek() == day.dayOfWeek()
        );
    }

    @Test
    @DisplayName("월 표시자가 생성된다")
    void whenExecuted_thenMonthIndicatorsAreCreated() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Map.of());

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        assertThat(response.months()).isNotEmpty();
        assertThat(response.months()).allMatch(month ->
                month.year() > 0 &&
                month.month() >= 1 &&
                month.month() <= 12
        );
    }

    @Test
    @DisplayName("주 인덱스가 0부터 순차적으로 증가한다")
    void whenExecuted_thenWeekIndicesAreSequential() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(Map.of());

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        for (int i = 0; i < response.weeks().size(); i++) {
            assertThat(response.weeks().get(i).weekIndex()).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("큰 활동 시간이 있어도 정상 처리된다")
    void whenLargeActivityMinutes_thenHandledCorrectly() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        LocalDate today = LocalDate.now();
        Map<LocalDate, Integer> sessionMinutes = new HashMap<>();
        sessionMinutes.put(today, 1440); // 24시간

        given(queryActivityMinutesPort.findSessionMinutesInPeriod(eq(TEST_USER_ID), any(), any()))
                .willReturn(sessionMinutes);

        // when
        WeeklyActivityResponse response = getWeeklyActivityService.execute();

        // then
        boolean foundActivity = response.weeks().stream()
                .flatMap(week -> week.days().stream())
                .anyMatch(day -> day.date().equals(today) && day.totalMinutes() == 1440);
        assertThat(foundActivity).isTrue();
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
