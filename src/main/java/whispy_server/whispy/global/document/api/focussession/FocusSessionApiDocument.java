package whispy_server.whispy.global.document.api.focussession;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionListResponse;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 집중 세션 CRUD 엔드포인트를 설명하는 Swagger 문서 전용 인터페이스이다.
 * 집중 세션 컨트롤러가 구현해 각 API 의 공통 설명과 보안 요건을 제공한다.
 */
@Tag(name = "FOCUS SESSION API", description = "집중 세션 관련 API")
public interface FocusSessionApiDocument {

    @Operation(
            summary = "집중 세션 저장",
            description = "사용자의 집중 세션 데이터를 저장합니다. " +
                    "음악 ID, 시작/종료 시간, 소요 시간, 태그 정보를 포함합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "집중 세션 저장 성공",
                    content = @Content(schema = @Schema(implementation = FocusSessionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "음악을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    FocusSessionResponse saveFocusSession(
            @RequestBody(description = "집중 세션 저장 요청", required = true,
                    content = @Content(schema = @Schema(implementation = SaveFocusSessionRequest.class)))
            SaveFocusSessionRequest request
    );

    @Operation(
            summary = "집중 세션 목록 조회",
            description = "현재 사용자의 집중 세션 목록을 최신순으로 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "집중 세션 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = FocusSessionListResponse.class))
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
    Page<FocusSessionListResponse> getFocusSessionList(Pageable pageable);

    @Operation(
            summary = "집중 세션 세부 조회",
            description = "특정 집중 세션의 세부 정보를 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "집중 세션 세부 조회 성공",
                    content = @Content(schema = @Schema(implementation = FocusSessionDetailResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "집중 세션을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    FocusSessionDetailResponse getFocusSessionDetail(
            @Parameter(description = "집중 세션 ID", required = true, in = ParameterIn.PATH) Long focusSessionId
    );

    @Operation(
            summary = "집중 세션 삭제",
            description = "특정 집중 세션을 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "집중 세션 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "집중 세션을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    void deleteFocusSession(
            @Parameter(description = "집중 세션 ID", required = true, in = ParameterIn.PATH) Long focusSessionId
    );
}
