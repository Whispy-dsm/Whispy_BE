package whispy_server.whispy.domain.music.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 생성 요청 DTO.
 *
 * 새로운 음악을 등록할 때 사용되는 요청 데이터입니다.
 *
 * @param title 음악 제목
 * @param filePath 음악 파일 경로
 * @param duration 음악 길이(초)
 * @param category 음악 카테고리
 * @param bannerImageUrl 배너 이미지 URL
 */
@Schema(description = "음악 생성 요청")
public record CreateMusicRequest(
        @Schema(description = "음악 제목", example = "편안한 빗소리", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 200)
        String title,
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
        String bannerImageUrl
) {}