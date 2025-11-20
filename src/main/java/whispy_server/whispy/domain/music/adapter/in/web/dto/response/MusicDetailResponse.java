package whispy_server.whispy.domain.music.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

@Schema(description = "음악 상세 응답")
public record MusicDetailResponse(
        @Schema(description = "음악 제목", example = "편안한 빗소리")
        String title,
        @Schema(description = "파일 경로", example = "/music/rain.mp3")
        String filePath,
        @Schema(description = "음악 길이(초)", example = "180")
        Integer duration,
        @Schema(description = "음악 카테고리")
        MusicCategory category,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/banner.jpg")
        String bannerImageUrl
) {

    public static MusicDetailResponse from(Music music) {
        return new MusicDetailResponse(
                music.title(),
                music.filePath(),
                music.duration(),
                music.category(),
                music.bannerImageUrl()
        );
    }
}