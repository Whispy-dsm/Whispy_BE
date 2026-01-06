package whispy_server.whispy.domain.music.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 수정 요청 DTO.
 *
 * 기존 음악 정보를 수정할 때 사용되는 요청 데이터입니다.
 *
 * @param id 수정할 음악 ID
 * @param title 음악 제목
 * @param artist 아티스트/제작자
 * @param description 음악 설명
 * @param filePath 음악 파일 경로
 * @param duration 음악 길이(초)
 * @param category 음악 카테고리
 * @param bannerImageUrl 배너 이미지 URL
 * @param videoUrl 뮤직 비디오 URL
 */
@Schema(description = "음악 수정 요청")
public record UpdateMusicRequest(
        @Schema(description = "음악 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long id,
        @Schema(description = "음악 제목", example = "편안한 빗소리", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 200)
        String title,
        @Schema(description = "아티스트/제작자", example = "Nature Sounds")
        @Size(max = 200)
        String artist,
        @Schema(description = "음악 설명", example = "부드러운 빗소리가 편안한 수면을 도와줍니다.")
        String description,
        @Schema(description = "파일 경로", example = "/music/rain.mp3", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 500)
        String filePath,
        @Schema(description = "음악 길이(초)", example = "180", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Min(1)
        Integer duration,
        @Schema(description = "음악 카테고리", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        MusicCategory category,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/banner.jpg")
        @Size(max = 500)
        String bannerImageUrl,
        @Schema(description = "뮤직 비디오 URL", example = "https://example.com/video.mp4")
        @Size(max = 500)
        String videoUrl
) {}
