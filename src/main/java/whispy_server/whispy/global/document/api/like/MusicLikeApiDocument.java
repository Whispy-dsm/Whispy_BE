package whispy_server.whispy.global.document.api.like;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.util.List;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 음악 좋아요 등록/조회 기능을 설명하는 Swagger 인터페이스이다.
 * 좋아요 컨트롤러는 이 계약을 구현해 문서화 정보를 자동으로 상속한다.
 */
@Tag(name = "MUSIC LIKE API", description = "음악 좋아요 관련 API")
public interface MusicLikeApiDocument {

    @Operation(
            summary = "음악 좋아요 토글",
            description = "음악에 좋아요를 추가하거나 제거합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "좋아요 토글 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void toggleMusicLike(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(
            summary = "내가 좋아요한 음악 목록 조회",
            description = "현재 사용자가 좋아요한 음악 목록을 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요한 음악 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = LikedMusicResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<LikedMusicResponse> getMyLikedMusics();

    @Operation(
            summary = "음악 좋아요 여부 확인",
            description = "현재 사용자가 특정 음악에 좋아요를 눌렀는지 확인합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 여부 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    boolean checkMusicLike(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);
}
