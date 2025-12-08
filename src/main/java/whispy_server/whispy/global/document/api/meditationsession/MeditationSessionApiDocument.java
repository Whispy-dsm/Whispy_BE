package whispy_server.whispy.global.document.api.meditationsession;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request.SaveMeditationSessionRequest;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionDetailResponse;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionListResponse;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 명상 세션 CRUD API 에 대한 Swagger 정의를 제공하는 인터페이스이다.
 */
@Tag(name = "MEDITATION SESSION API", description = "명상 세션 관련 API")
public interface MeditationSessionApiDocument {

    @Operation(
            summary = "명상 세션 저장",
            description = "사용자의 명상 세션 데이터를 저장합니다. " +
                    "시작/종료 시간, 소요 시간, 호흡 모드 정보를 포함합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "명상 세션 저장 성공",
                    content = @Content(schema = @Schema(implementation = MeditationSessionResponse.class))
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
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    MeditationSessionResponse saveMeditationSession(@Valid SaveMeditationSessionRequest request);

    @Operation(
            summary = "명상 세션 목록 조회",
            description = "현재 사용자의 명상 세션 목록을 최신순으로 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "명상 세션 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = MeditationSessionListResponse.class))
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
    Page<MeditationSessionListResponse> getMeditationSessionList(Pageable pageable);

    @Operation(
            summary = "명상 세션 세부 조회",
            description = "특정 명상 세션의 세부 정보를 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "명상 세션 세부 조회 성공",
                    content = @Content(schema = @Schema(implementation = MeditationSessionDetailResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "명상 세션을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    MeditationSessionDetailResponse getMeditationSessionDetail(
            @Parameter(description = "명상 세션 ID", required = true, in = ParameterIn.PATH) Long meditationSessionId
    );

    @Operation(
            summary = "명상 세션 삭제",
            description = "특정 명상 세션을 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "명상 세션 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "명상 세션을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    void deleteMeditationSession(
            @Parameter(description = "명상 세션 ID", required = true, in = ParameterIn.PATH) Long meditationSessionId
    );
}
