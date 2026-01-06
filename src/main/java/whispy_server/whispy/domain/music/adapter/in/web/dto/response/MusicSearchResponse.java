package whispy_server.whispy.domain.music.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 검색 응답 DTO.
 *
 * 음악 검색 시 반환되는 응답 데이터입니다.
 *
 * @param id 음악 ID
 * @param title 음악 제목
 * @param artist 아티스트/제작자
 * @param description 음악 설명
 * @param filePath 음악 파일 경로
 * @param duration 음악 길이(초)
 * @param category 음악 카테고리
 * @param bannerImageUrl 배너 이미지 URL
 * @param videoUrl 뮤직 비디오 URL
 */
@Schema(description = "음악 검색 응답")
public record MusicSearchResponse(
        @Schema(description = "음악 ID", example = "1")
        Long id,
        @Schema(description = "음악 제목", example = "편안한 빗소리")
        String title,
        @Schema(description = "아티스트/제작자", example = "Nature Sounds")
        String artist,
        @Schema(description = "음악 설명", example = "부드러운 빗소리가 편안한 수면을 도와줍니다.")
        String description,
        @Schema(description = "파일 경로", example = "/music/rain.mp3")
        String filePath,
        @Schema(description = "음악 길이(초)", example = "180")
        Integer duration,
        @Schema(description = "음악 카테고리")
        MusicCategory category,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/banner.jpg")
        String bannerImageUrl,
        @Schema(description = "뮤직 비디오 URL", example = "https://example.com/video.mp4")
        String videoUrl
) {

    /**
     * Music 도메인 모델을 응답 DTO로 변환합니다.
     *
     * @param music 음악 도메인 모델
     * @return 음악 검색 응답 DTO
     */
    public static MusicSearchResponse from(Music music) {
        return new MusicSearchResponse(
                music.id(),
                music.title(),
                music.artist(),
                music.description(),
                music.filePath(),
                music.duration(),
                music.category(),
                music.bannerImageUrl(),
                music.videoUrl()
        );
    }
}