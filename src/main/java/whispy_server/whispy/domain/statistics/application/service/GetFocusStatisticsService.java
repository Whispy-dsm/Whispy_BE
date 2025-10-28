package whispy_server.whispy.domain.statistics.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.adapter.out.dto.FocusSessionDto;
import whispy_server.whispy.domain.statistics.application.port.in.GetFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.model.FocusSessionSummary;
import whispy_server.whispy.domain.statistics.model.FocusStatistics;
import whispy_server.whispy.domain.statistics.model.types.PeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFocusStatisticsService implements GetFocusStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;

    @Override
    @Transactional(readOnly = true)
    public FocusStatisticsResponse execute(PeriodType period, LocalDate date) {
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        List<FocusSessionDto> sessions = queryFocusStatisticsPort.findByUserIdAndPeriod(user.id(), start, end);

        int todayMinutes = sessions.stream()
                .filter(s -> s.startedAt().toLocalDate().equals(date))
                .mapToInt(s -> s.durationSeconds() / 60)
                .sum();

        int totalCount = sessions.size();
        int totalMinutes = sessions.stream()
                .mapToInt(s -> s.durationSeconds() / 60)
                .sum();

        Map<FocusTag, Integer> tagMinutes = calculateTagMinutes(sessions);

        List<FocusSessionSummary> sessionSummaries = sessions.stream()
                .map(dto -> new FocusSessionSummary(
                        dto.startedAt(),
                        dto.durationSeconds() / 60,
                        dto.tag()
                ))
                .toList();

        FocusStatistics statistics = new FocusStatistics(
                totalCount,
                totalMinutes,
                todayMinutes,
                tagMinutes,
                sessionSummaries
        );

        return FocusStatisticsResponse.from(statistics);
    }

    private LocalDateTime[] calculatePeriodRange(PeriodType period, LocalDate date) {
        return switch (period) {
            case TODAY -> new LocalDateTime[]{
                    date.atStartOfDay(),
                    date.atTime(23, 59, 59)
            };
            case WEEK -> new LocalDateTime[]{
                    date.with(DayOfWeek.MONDAY).atStartOfDay(),
                    date.with(DayOfWeek.SUNDAY).atTime(23, 59, 59)
            };
            case MONTH -> new LocalDateTime[]{
                    date.withDayOfMonth(1).atStartOfDay(),
                    date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59)
            };
            case YEAR -> new LocalDateTime[]{
                    date.withDayOfYear(1).atStartOfDay(),
                    date.withDayOfYear(date.lengthOfYear()).atTime(23, 59, 59)
            };
        };
    }

    private Map<FocusTag, Integer> calculateTagMinutes(List<FocusSessionDto> sessions) {
        Map<FocusTag, Integer> tagMinutes = new EnumMap<>(FocusTag.class);

        for(FocusTag tag : FocusTag.values()) {
            tagMinutes.put(tag, 0);
        }

        sessions.stream()
                .collect(Collectors.groupingBy(
                        FocusSessionDto::tag,
                        Collectors.summingInt(s -> s.durationSeconds() / 60)
                ))
                .forEach(tagMinutes::put);

        return tagMinutes;
    }
}
