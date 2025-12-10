package whispy_server.whispy.domain.announcement.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 공지사항 생성 요청 DTO.
 *
 * 새로운 공지사항을 생성하기 위한 요청 데이터를 담고 있습니다.
 */
@Schema(description = "공지사항 생성 요청")
public record CreateAnnouncementRequest(
        @Schema(description = "공지사항 제목", example = "새로운 업데이트 안내", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 200)
        String title,
        @Schema(description = "공지사항 내용", example = "새로운 기능이 추가되었습니다.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 10000)
        String content,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/banner.jpg")
        @Size(max = 500)
        String bannerImageUrl
) {
}
