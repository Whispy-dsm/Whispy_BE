package whispy_server.whispy.domain.statistics.focus.summary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.common.util.StatisticsPeriodRangeCalculator;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.in.GetFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.summary.model.FocusStatistics;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 집중 세션을 기간별로 집계해 주요 지표와 태그별 시간을 계산하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetFocusStatisticsService implements GetFocusStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    @Transactional(readOnly = true)
    @UserAction("집중 통계 조회")
    public FocusStatisticsResponse execute(FocusPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        FocusAggregationDto aggregation = queryFocusStatisticsPort.aggregateByPeriod(user.id(), start, end);
        int todayMinutes = queryFocusStatisticsPort.sumMinutesByDate(user.id(), date);
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
        return StatisticsPeriodRangeCalculator.calculateFocusPeriodRange(period, date);
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
