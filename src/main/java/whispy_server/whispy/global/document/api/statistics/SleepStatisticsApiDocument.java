package whispy_server.whispy.global.document.api.statistics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response.DailySleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response.SleepPeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.time.LocalDate;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "SLEEP STATISTICS API", description = "수면 통계 관련 API")
public interface SleepStatisticsApiDocument {

    @Operation(
            summary = "수면 통계 조회",
            description = "지정된 기간에 대한 수면 세션 통계 데이터를 조회합니다. " +
                    "오늘의 수면 시간, 평균 수면 시간, 수면 일정성, 평균 입면/기상 시간, 총 수면 시간을 제공합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수면 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = SleepStatisticsResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 기간 타입 또는 날짜 형식)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    SleepStatisticsResponse getSleepStatistics(
            @Parameter(description = "통계 기간 타입 (WEEK: 주간, MONTH: 월간, YEAR: 연간)", required = true, example = "WEEK")
            SleepPeriodType period,

            @Parameter(description = "기준 날짜 (yyyy-MM-dd)", required = true, example = "2025-10-28")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );

    @Operation(
            summary = "수면 기간별 비교 통계 조회",
            description = "현재 기간, 이전 기간, 이전이전 기간의 수면 시간을 비교합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "비교 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = SleepPeriodComparisonResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 기간 타입 또는 날짜 형식)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    SleepPeriodComparisonResponse getSleepPeriodComparison(
            @Parameter(description = "비교 기간 타입 (WEEK: 주간, MONTH: 월간, YEAR: 연간)", required = true, example = "WEEK")
            SleepPeriodType period,

            @Parameter(description = "기준 날짜 (yyyy-MM-dd)", required = true, example = "2025-10-28")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );

    @Operation(
            summary = "일별/월별 수면 통계 조회",
            description = "그래프 표시를 위한 집계 데이터를 조회합니다. " +
                    "WEEK: 요일별(월~일) 집계, " +
                    "MONTH: 날짜별(1~말일) 집계, " +
                    "YEAR: 월별(1~12월) 집계",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일별 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = DailySleepStatisticsResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 기간 타입 또는 날짜 형식)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    DailySleepStatisticsResponse getDailySleepStatistics(
            @Parameter(
                    description = "통계 기간 타입 (WEEK: 요일별, MONTH: 날짜별, YEAR: 월별)",
                    required = true,
                    example = "WEEK"
            )
            SleepPeriodType period,

            @Parameter(
                    description = "기준 날짜 (yyyy-MM-dd)",
                    required = true,
                    example = "2025-10-28"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );
}
