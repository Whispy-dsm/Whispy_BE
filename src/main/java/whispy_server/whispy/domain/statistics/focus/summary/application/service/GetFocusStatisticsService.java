package whispy_server.whispy.domain.statistics.focus.summary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
import whispy_server.whispy.global.config.redis.RedisConfig;
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
    @Cacheable(
            value = RedisConfig.STATS_FOCUS_SUMMARY_CACHE,
            keyGenerator = "statisticsSummaryKeyGenerator",
            sync = true
    )
    public FocusStatisticsResponse execute(FocusPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        FocusAggregationDto aggregation = queryFocusStatisticsPort.aggregateByPeriod(user.id(), start, end);
        int todayMinutes = queryFocusStatisticsPort.sumMinutesByDate(user.id(), date);
        int totalDays = queryFocusStatisticsPort.countDistinctDays(user.id(), start, end);
        List<TagMinutesDto> tagAggregations = queryFocusStatisticsPort.aggregateByTag(user.id(), start, end);

        int totalCount = aggregation.totalCount();
        int totalMinutes = aggregation.totalMinutes();
        Map<FocusTag, Integer> tagMinutes = convertToTagMap(tagAggregations);

        FocusStatistics statistics = new FocusStatistics(
                totalCount,
                totalMinutes,
                todayMinutes,
                totalDays,
                tagMinutes
        );

        return FocusStatisticsResponse.from(statistics);
    }

    /**
     * 집중 통계 조회 기간을 계산합니다.
     *
     * 기간 타입(DAILY, WEEKLY, MONTHLY)에 따라 시작일시와 종료일시를 계산합니다.
     * 집중 세션은 일반적으로 당일 00:00부터 23:59까지를 하나의 단위로 처리합니다.
     *
     * @param period 통계 기간 타입 (일간/주간/월간)
     * @param date   기준 날짜
     * @return [시작일시, 종료일시] 배열
     */
    private LocalDateTime[] calculatePeriodRange(FocusPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateFocusPeriodRange(period, date);
    }

    /**
     * 태그별 집중 시간 DTO 리스트를 EnumMap으로 변환합니다.
     *
     * DB 집계 결과는 실제 기록이 있는 태그만 포함하므로,
     * 모든 FocusTag enum 값을 0으로 초기화한 후 집계 데이터로 덮어씁니다.
     * 이를 통해 클라이언트가 모든 태그에 대한 값을 받을 수 있습니다.
     *
     * 예시:
     * - DB 집계: [{WORK: 120}, {STUDY: 60}]
     * - 결과: {WORK: 120, STUDY: 60, READING: 0, EXERCISE: 0, ...}
     *
     * @param aggregations DB에서 조회한 태그별 집중 시간 목록
     * @return 모든 FocusTag를 키로 가지는 Map (값이 없는 태그는 0)
     */
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
