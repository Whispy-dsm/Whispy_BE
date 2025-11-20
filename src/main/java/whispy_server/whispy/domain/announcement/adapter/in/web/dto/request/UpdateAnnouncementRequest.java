package whispy_server.whispy.domain.announcement.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지사항 수정 요청")
public record UpdateAnnouncementRequest(
        @Schema(description = "공지사항 ID", example = "1")
        Long id,
        @Schema(description = "공지사항 제목", example = "업데이트된 공지사항")
        String title,
        @Schema(description = "공지사항 내용", example = "수정된 내용입니다.")
        String content,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/banner.jpg")
        String bannerImageUrl
) {
}
