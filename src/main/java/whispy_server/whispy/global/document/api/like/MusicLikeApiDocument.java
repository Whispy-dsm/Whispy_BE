package whispy_server.whispy.global.document.api.like;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;

import java.util.List;

@Tag(name = "MUSIC LIKE API", description = "음악 좋아요 관련 API")
public interface MusicLikeApiDocument {

    @Operation(summary = "음악 좋아요 토글", description = "음악에 좋아요를 추가하거나 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "좋아요 토글 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void toggleMusicLike(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(summary = "내가 좋아요한 음악 목록 조회", description = "현재 사용자가 좋아요한 음악 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요한 음악 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = LikedMusicResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    List<LikedMusicResponse> getMyLikedMusics();
}
