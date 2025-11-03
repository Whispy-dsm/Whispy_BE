package whispy_server.whispy.domain.statistics.focus.summary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.in.GetFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.summary.model.FocusStatistics;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.TagMinutesDto;
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

@Service
@RequiredArgsConstructor
public class GetFocusStatisticsService implements GetFocusStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;

    @Override
    @Transactional(readOnly = true)
    public FocusStatisticsResponse execute(FocusPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);
        
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        FocusAggregationDto aggregation = queryFocusStatisticsPort.aggregateByPeriod(user.id(), start, end);
        Integer todayMinutes = queryFocusStatisticsPort.sumMinutesByDate(user.id(), date);
        List<TagMinutesDto> tagAggregations = queryFocusStatisticsPort.aggregateByTag(user.id(), start, end);

        int totalCount = aggregation.totalCount();
        int totalMinutes = aggregation.totalMinutes();
        Map<FocusTag, Integer> tagMinutes = convertToTagMap(tagAggregations);

        FocusStatistics statistics = new FocusStatistics(
                totalCount,
                totalMinutes,
                todayMinutes,
                tagMinutes
        );

        return FocusStatisticsResponse.from(statistics);
    }

    private LocalDateTime[] calculatePeriodRange(FocusPeriodType period, LocalDate date) {
        return switch (period) {
            case TODAY -> new LocalDateTime[]{
                    date.atStartOfDay(),
                    date.atTime(TimeConstants.END_OF_DAY)
            };
            case WEEK -> new LocalDateTime[]{
                    date.with(DayOfWeek.MONDAY).atStartOfDay(),
                    date.with(DayOfWeek.SUNDAY).atTime(TimeConstants.END_OF_DAY)
            };
            case MONTH -> new LocalDateTime[]{
                    date.withDayOfMonth(1).atStartOfDay(),
                    date.withDayOfMonth(date.lengthOfMonth()).atTime(TimeConstants.END_OF_DAY)
            };
            case YEAR -> new LocalDateTime[]{
                    date.withDayOfYear(1).atStartOfDay(),
                    date.withDayOfYear(date.lengthOfYear()).atTime(TimeConstants.END_OF_DAY)
            };
        };
    }

    private Map<FocusTag, Integer> convertToTagMap(List<TagMinutesDto> aggregations) {
        Map<FocusTag, Integer> tagMinutes = new EnumMap<>(FocusTag.class);

        for (FocusTag tag : FocusTag.values()) {
            tagMinutes.put(tag, 0);
        }

        for (TagMinutesDto dto : aggregations) {
            tagMinutes.put(dto.tag(), dto.minutes());
        }

        return tagMinutes;
    }
}
