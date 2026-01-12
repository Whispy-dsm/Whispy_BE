package whispy_server.whispy.domain.announcement.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 공지사항 수정 요청 DTO.
 *
 * 기존 공지사항의 정보를 수정하기 위한 요청 데이터를 담고 있습니다.
 * content는 마크다운 형식으로 작성할 수 있으며, 이미지는 마크다운 문법으로 포함 가능합니다.
 */
@Schema(description = "공지사항 수정 요청")
public record UpdateAnnouncementRequest(
        @Schema(description = "공지사항 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long id,
        @Schema(description = "공지사항 제목", example = "업데이트된 공지사항", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 200)
        String title,
        @Schema(description = "공지사항 내용 (마크다운)", example = "![수정된 배너](https://example.com/new-banner.jpg)\n\n수정된 내용입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 10000)
        String content
) {
}
