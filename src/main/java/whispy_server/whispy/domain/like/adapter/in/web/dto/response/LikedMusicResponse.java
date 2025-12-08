package whispy_server.whispy.domain.like.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.like.adapter.out.dto.MusicLikeWithMusicDto;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

import java.util.List;

/**
 * 좋아요한 음악 목록을 내려줄 때 사용하는 응답 DTO.
 */
@Schema(description = "좋아요한 음악 응답")
public record LikedMusicResponse(
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

        public static LikedMusicResponse from(MusicLikeWithMusicDto dto) {
                return new LikedMusicResponse(
                        dto.musicId(),
                        dto.title(),
                        dto.filePath(),
                        dto.duration(),
                        dto.category()
                );
        }

        public static List<LikedMusicResponse> fromList(List<MusicLikeWithMusicDto> dtos) {
                return dtos.stream()
                        .map(LikedMusicResponse::from)
                        .toList();
        }
}
