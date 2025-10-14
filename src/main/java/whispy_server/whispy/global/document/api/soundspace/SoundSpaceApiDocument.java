package whispy_server.whispy.global.document.api.soundspace;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.SoundSpaceMusicResponse;

import java.util.List;

@Tag(name = "SOUND SPACE API", description = "사운드 스페이스 관련 API")
public interface SoundSpaceApiDocument {

    @Operation(summary = "사운드 스페이스 음악 목록 조회", description = "현재 사용자의 사운드 스페이스에 저장된 음악 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사운드 스페이스 음악 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = SoundSpaceMusicResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    List<SoundSpaceMusicResponse> getSoundSpaceMusics();

    @Operation(summary = "사운드 스페이스 음악 토글", description = "사운드 스페이스에 음악을 추가하거나 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "음악 토글 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void toggleSoundSpaceMusic(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(summary = "음악 존재 여부 확인", description = "해당 음악이 사운드 스페이스에 존재하는지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 존재 여부 확인 성공",
                    content = @Content(schema = @Schema(implementation = MusicInSoundSpaceCheckResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    MusicInSoundSpaceCheckResponse checkMusicInSoundSpace(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);

    @Operation(summary = "사운드 스페이스 음악 삭제", description = "사운드 스페이스에서 음악을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "음악 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void removeMusicFromSoundSpace(@Parameter(description = "음악 ID", required = true, in = ParameterIn.PATH) Long musicId);
}
