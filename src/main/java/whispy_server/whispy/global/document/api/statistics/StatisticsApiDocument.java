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
import whispy_server.whispy.domain.statistics.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.model.types.PeriodType;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.time.LocalDate;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

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
            PeriodType period,

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
}
