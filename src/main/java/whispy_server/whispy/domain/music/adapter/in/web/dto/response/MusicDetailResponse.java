package whispy_server.whispy.domain.music.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 상세 응답 DTO.
 *
 * 특정 음악의 상세 정보 조회 시 반환되는 응답 데이터입니다.
 *
 * @param title 음악 제목
 * @param filePath 음악 파일 경로
 * @param duration 음악 길이(초)
 * @param category 음악 카테고리
 * @param bannerImageUrl 배너 이미지 URL
 */
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

    /**
     * Music 도메인 모델을 응답 DTO로 변환합니다.
     *
     * @param music 음악 도메인 모델
     * @return 음악 상세 응답 DTO
     */
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