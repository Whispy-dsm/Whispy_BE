package whispy_server.whispy.domain.statistics.activity.applicatoin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.in.GetWeeklySessionExistsUseCase;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.CheckWeeklySessionExistsPort;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 주간 세션 존재 여부 조회 서비스.
 *
 * 사용자의 현재 주의 요일별 세션 존재 여부를 조회하는 애플리케이션 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class GetWeeklySessionExistsService implements GetWeeklySessionExistsUseCase {

    private final CheckWeeklySessionExistsPort checkWeeklySessionExistsPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 요일별 세션 존재 여부를 조회합니다.
     *
     * @return 주간 세션 존재 여부 응답
     */
    @UserAction("주간 세션 존재 여부 조회")
    @Override
    @Transactional(readOnly = true)
    public WeeklySessionExistsResponse execute() {
        Long userId = userFacadeUseCase.currentUser().id();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(TimeConstants.END_OF_DAY);

        Set<LocalDate> sessionDates = checkWeeklySessionExistsPort.findSessionDatesInPeriod(
                userId,
                startDateTime,
                endDateTime
        );

        Map<DayOfWeek, Boolean> weeklyExists = IntStream.range(0, 7)
                .mapToObj(startOfWeek::plusDays)
                .collect(Collectors.toMap(
                        LocalDate::getDayOfWeek,
                        sessionDates::contains,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        return new WeeklySessionExistsResponse(weeklyExists);
    }
}
