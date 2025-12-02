package whispy_server.whispy.global.document.api.statistics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklyActivityResponse;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 주간 세션 존재 여부와 활동량 지표를 문서화하는 Swagger 인터페이스이다.
 * 활동 통계 컨트롤러는 본 인터페이스를 구현해 API 설명을 재사용한다.
 */
@Tag(name = "ACTIVITY STATISTICS API", description = "활동 통계 관련 API")
public interface ActivityStatisticsApiDocument {

    @Operation(
            summary = "1주일 세션 존재 여부 조회",
            description = "월요일부터 일요일까지 각 요일별로 집중 세션 또는 수면 세션이 존재하는지 확인합니다. 둘 중 하나라도 있으면 true를 반환합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WeeklySessionExistsResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    WeeklySessionExistsResponse getWeeklySessionExists();

    @Operation(
            summary = "20주 활동 현황 조회",
            description = "깃허브 잔디처럼 최근 20주(140일) 동안의 세션 활동 현황을 조회합니다. 월요일부터 일요일까지 완전한 주 단위로 제공됩니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WeeklyActivityResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    WeeklyActivityResponse getWeeklyActivity();
}
