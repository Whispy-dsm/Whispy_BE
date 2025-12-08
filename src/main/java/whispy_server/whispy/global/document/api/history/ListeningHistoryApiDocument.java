package whispy_server.whispy.global.document.api.history;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 음악 청취 기록과 관련된 REST 엔드포인트의 Swagger 메타데이터를 정의한다.
 * 기록 저장/조회 컨트롤러는 이 인터페이스를 구현해 문서 정보를 재사용한다.
 */
@Tag(name = "LISTENING HISTORY API", description = "음악 청취 기록 관련 API")
public interface ListeningHistoryApiDocument {

    @Operation(
            summary = "음악 청취 기록",
            description = "사용자가 음악을 들었을 때 기록을 저장합니다. 같은 음악을 다시 들으면 청취 시간이 업데이트됩니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "청취 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void recordListening(
            @Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(
            summary = "내 청취 기록 조회",
            description = "현재 사용자의 음악 청취 기록을 최신순으로 조회합니다. 페이징을 지원합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "청취 기록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ListeningHistoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Page<ListeningHistoryResponse> getMyHistory(
            @Parameter(description = "페이징 정보 (page, size)", in = ParameterIn.QUERY) Pageable pageable);
}
