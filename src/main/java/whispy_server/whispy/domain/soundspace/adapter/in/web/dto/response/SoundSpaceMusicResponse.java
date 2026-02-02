package whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.soundspace.adapter.out.dto.SoundSpaceMusicWithDetailDto;

import java.util.List;

/**
 * 사운드 스페이스 목록을 노출할 때 사용하는 응답 DTO.
 */
@Schema(description = "사운드 스페이스 음악 응답")
public record SoundSpaceMusicResponse(
        @Schema(description = "음악 ID", example = "1")
        Long musicId,
        @Schema(description = "음악 제목", example = "편안한 빗소리")
        String title,
        @Schema(description = "파일 경로", example = "/music/rain.mp3")
        String filePath,
        @Schema(description = "음악 길이(초)", example = "180")
        Integer duration,
        @Schema(description = "음악 카테고리")
        MusicCategory category
) {
    /**
     * SoundSpaceMusicWithDetailDto 리스트를 SoundSpaceMusicResponse 리스트로 변환합니다.
     *
     * @param dtos 사운드 스페이스 + 음악 상세 정보 DTO 리스트
     * @return 사운드 스페이스 음악 응답 DTO 리스트
     */
    public static List<SoundSpaceMusicResponse> fromList(List<SoundSpaceMusicWithDetailDto> dtos) {
        return dtos.stream()
                .map(dto -> new SoundSpaceMusicResponse(
                        dto.musicId(),
                        dto.title(),
                        dto.filePath(),
                        dto.duration(),
                        dto.category()
                ))
                .toList();
    }
}
