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
import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.time.LocalDate;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 포커스 통계 조회 엔드포인트의 Swagger 스펙을 모아둔 인터페이스이다.
 * 통계 컨트롤러는 이 인터페이스를 구현하여 기간·비교·일별 통계 API 설명을 공유한다.
 */
@Tag(name = "STATISTICS API", description = "통계 관련 API")
public interface StatisticsApiDocument {

    @Operation(
            summary = "포커스 세션 통계 조회",
            description = "지정된 기간에 대한 포커스 세션 통계 데이터를 조회합니다. " +
                    "일간(DAILY), 주간(WEEKLY), 월간(MONTHLY) 통계를 제공합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = FocusStatisticsResponse.class))
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
    FocusStatisticsResponse getFocusStatistics(
            @Parameter(
                    description = "통계 기간 타입 (DAILY: 일간, WEEKLY: 주간, MONTHLY: 월간)",
                    required = true,
                    example = "WEEKLY"
            )
            FocusPeriodType period,

            @Parameter(
                    description = "기준 날짜 (ISO 8601 형식: yyyy-MM-dd). " +
                            "DAILY: 해당 날짜의 통계, " +
                            "WEEKLY: 해당 날짜가 속한 주의 통계, " +
                            "MONTHLY: 해당 날짜가 속한 월의 통계",
                    required = true,
                    example = "2025-10-28"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );

    @Operation(
            summary = "기간별 비교 통계 조회",
            description = "현재 기간, 이전 기간, 이전이전 기간(2번째 이전)의 집중 시간을 비교합니다. " +
                    "주간(WEEKLY), 월간(MONTHLY), 연간(YEARLY) 비교 통계를 제공합니다. " +
                    "WEEKLY: 이번주/지난주/저저번주, " +
                    "MONTHLY: 이번달/지난달/저저번달, " +
                    "YEARLY: 올해/작년/재작년",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "비교 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = PeriodComparisonResponse.class))
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
    PeriodComparisonResponse getPeriodComparison(
            @Parameter(
                    description = "비교 기간 타입 (WEEKLY: 주간, MONTHLY: 월간, YEARLY: 연간)",
                    required = true,
                    example = "WEEKLY"
            )
            FocusPeriodType period,

            @Parameter(
                    description = "기준 날짜 (ISO 8601 형식: yyyy-MM-dd). " +
                            "해당 날짜가 속한 기간을 기준으로 이전 기간들과 비교합니다.",
                    required = true,
                    example = "2025-10-28"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );

    @Operation(
            summary = "일별/시간별/월별 포커스 통계 조회",
            description = "그래프 표시를 위한 집계 데이터를 조회합니다. " +
                    "TODAY: 시간별(0~23시) 집계, " +
                    "WEEK: 요일별(월~일) 집계, " +
                    "MONTH: 날짜별(1~말일) 집계, " +
                    "YEAR: 월별(1~12월) 집계",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일별 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = DailyFocusStatisticsResponse.class))
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
    DailyFocusStatisticsResponse getDailyFocusStatistics(
            @Parameter(
                    description = "통계 기간 타입 (TODAY: 시간별, WEEK: 요일별, MONTH: 날짜별, YEAR: 월별)",
                    required = true,
                    example = "WEEK"
            )
            FocusPeriodType period,

            @Parameter(
                    description = "기준 날짜 (yyyy-MM-dd)",
                    required = true,
                    example = "2025-10-28"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );
}
