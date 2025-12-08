package whispy_server.whispy.global.document.api.soundspace;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.SoundSpaceMusicResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.util.List;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 사운드 스페이스 음악 관리 엔드포인트의 Swagger 스펙을 정의한다.
 * 사운드 스페이스 컨트롤러에서 구현하여 조회/토글/삭제 API 문서를 일관되게 유지한다.
 */
@Tag(name = "SOUND SPACE API", description = "사운드 스페이스 관련 API")
public interface SoundSpaceApiDocument {

    @Operation(
            summary = "사운드 스페이스 음악 목록 조회",
            description = "현재 사용자의 사운드 스페이스에 저장된 음악 목록을 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사운드 스페이스 음악 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = SoundSpaceMusicResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<SoundSpaceMusicResponse> getSoundSpaceMusics();

    @Operation(
            summary = "사운드 스페이스 음악 토글",
            description = "사운드 스페이스에 음악을 추가하거나 제거합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "음악 토글 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void toggleSoundSpaceMusic(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(
            summary = "음악 존재 여부 확인",
            description = "해당 음악이 사운드 스페이스에 존재하는지 확인합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 존재 여부 확인 성공",
                    content = @Content(schema = @Schema(implementation = MusicInSoundSpaceCheckResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    MusicInSoundSpaceCheckResponse checkMusicInSoundSpace(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(
            summary = "사운드 스페이스 음악 삭제",
            description = "사운드 스페이스에서 음악을 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "음악 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void removeMusicFromSoundSpace(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);
}
