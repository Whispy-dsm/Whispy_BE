package whispy_server.whispy.domain.statistics.activity.applicatoin.service.unit;
import whispy_server.whispy.domain.statistics.activity.applicatoin.service.GetWeeklySessionExistsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.CheckWeeklySessionExistsPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

/**
 * GetWeeklySessionExistsService의 단위 테스트 클래스
 *
 * 주간 세션 존재 여부 조회 서비스의 다양한 시나리오를 검증합니다.
 * 요일별 세션 존재 여부 확인 및 Map 변환 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetWeeklySessionExistsService 테스트")
class GetWeeklySessionExistsServiceTest {

    @InjectMocks
    private GetWeeklySessionExistsService getWeeklySessionExistsService;

    @Mock
    private CheckWeeklySessionExistsPort checkWeeklySessionExistsPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("세션이 전혀 없을 때 모든 요일이 false이다")
    void whenNoSessionsExist_thenAllDaysAreFalse() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(checkWeeklySessionExistsPort.findSessionDatesInPeriod(
                eq(TEST_USER_ID),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(Collections.emptySet());

        // when
        WeeklySessionExistsResponse response = getWeeklySessionExistsService.execute();

        // then
        assertThat(response.weeklyExists()).hasSize(7);
        assertThat(response.weeklyExists().values()).allMatch(exists -> !exists);
        assertThat(response.weeklyExists()).containsKeys(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );
    }

    @Test
    @DisplayName("특정 날짜에만 세션이 있을 때 해당 요일만 true이다")
    void whenSessionsExistOnSpecificDays_thenOnlyThoseDaysAreTrue() {
        // given
        User user = createUser();
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate wednesday = today.with(DayOfWeek.WEDNESDAY);
        LocalDate friday = today.with(DayOfWeek.FRIDAY);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(checkWeeklySessionExistsPort.findSessionDatesInPeriod(
                eq(TEST_USER_ID),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(Set.of(monday, wednesday, friday));

        // when
        WeeklySessionExistsResponse response = getWeeklySessionExistsService.execute();

        // then
        assertThat(response.weeklyExists()).hasSize(7);
        assertThat(response.weeklyExists().get(DayOfWeek.MONDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.TUESDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.WEDNESDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.THURSDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.FRIDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.SATURDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.SUNDAY)).isFalse();
    }

    @Test
    @DisplayName("모든 날짜에 세션이 있을 때 모든 요일이 true이다")
    void whenSessionsExistOnAllDays_thenAllDaysAreTrue() {
        // given
        User user = createUser();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        Set<LocalDate> allDaysOfWeek = Set.of(
                startOfWeek,
                startOfWeek.plusDays(1),
                startOfWeek.plusDays(2),
                startOfWeek.plusDays(3),
                startOfWeek.plusDays(4),
                startOfWeek.plusDays(5),
                startOfWeek.plusDays(6)
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(checkWeeklySessionExistsPort.findSessionDatesInPeriod(
                eq(TEST_USER_ID),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(allDaysOfWeek);

        // when
        WeeklySessionExistsResponse response = getWeeklySessionExistsService.execute();

        // then
        assertThat(response.weeklyExists()).hasSize(7);
        assertThat(response.weeklyExists().values()).allMatch(exists -> exists);
    }

    @Test
    @DisplayName("주말에만 세션이 있을 때 토요일과 일요일만 true이다")
    void whenSessionsExistOnWeekendsOnly_thenOnlyWeekendsAreTrue() {
        // given
        User user = createUser();
        LocalDate today = LocalDate.now();
        LocalDate saturday = today.with(DayOfWeek.SATURDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(checkWeeklySessionExistsPort.findSessionDatesInPeriod(
                eq(TEST_USER_ID),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(Set.of(saturday, sunday));

        // when
        WeeklySessionExistsResponse response = getWeeklySessionExistsService.execute();

        // then
        assertThat(response.weeklyExists()).hasSize(7);
        assertThat(response.weeklyExists().get(DayOfWeek.MONDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.TUESDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.WEDNESDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.THURSDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.FRIDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.SATURDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.SUNDAY)).isTrue();
    }

    @Test
    @DisplayName("평일에만 세션이 있을 때 월~금만 true이다")
    void whenSessionsExistOnWeekdaysOnly_thenOnlyWeekdaysAreTrue() {
        // given
        User user = createUser();
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        Set<LocalDate> weekdays = Set.of(
                monday,
                monday.plusDays(1),
                monday.plusDays(2),
                monday.plusDays(3),
                monday.plusDays(4)
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(checkWeeklySessionExistsPort.findSessionDatesInPeriod(
                eq(TEST_USER_ID),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(weekdays);

        // when
        WeeklySessionExistsResponse response = getWeeklySessionExistsService.execute();

        // then
        assertThat(response.weeklyExists()).hasSize(7);
        assertThat(response.weeklyExists().get(DayOfWeek.MONDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.TUESDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.WEDNESDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.THURSDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.FRIDAY)).isTrue();
        assertThat(response.weeklyExists().get(DayOfWeek.SATURDAY)).isFalse();
        assertThat(response.weeklyExists().get(DayOfWeek.SUNDAY)).isFalse();
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
