package whispy_server.whispy.global.document.api.music;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 음악 조회/검색 API 의 Swagger 명세를 중앙에서 관리하기 위한 인터페이스이다.
 */
@Tag(name = "MUSIC API", description = "음악 관련 API")
public interface MusicApiDocument {

    @Operation(
            summary = "음악 세부 조회",
            description = "음악 ID로 세부 정보를 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 조회 성공",
                    content = @Content(schema = @Schema(implementation = MusicDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    MusicDetailResponse getMusicDetail(
            @Parameter(description = "음악 ID", required = true) Long id
    );

    @Operation(
            summary = "키워드로 음악 검색",
            description = "음악 제목에 포함된 키워드로 음악을 검색합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 검색 성공",
                    content = @Content(schema = @Schema(implementation = MusicSearchResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 파라미터 타입 불일치)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Page<MusicSearchResponse> searchMusic(
            @Parameter(description = "검색할 키워드", required = true) String keyword,
            Pageable pageable
    );

    @Operation(
            summary = "카테고리로 음악 검색",
            description = "지정된 카테고리로 음악을 검색합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 검색 성공",
                    content = @Content(schema = @Schema(implementation = MusicSearchResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 유효하지 않은 카테고리)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Page<MusicSearchResponse> searchByMusicCategory(
            @Parameter(description = "검색할 음악 카테고리", required = true) MusicCategory musicCategory,
            Pageable pageable
    );
}
