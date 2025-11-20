package whispy_server.whispy.domain.announcement.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.announcement.model.Announcement;

@Schema(description = "전체 공지사항 조회 응답")
public record QueryAllAnnouncementResponse(
        @Schema(description = "공지사항 ID", example = "1")
        Long id,
        @Schema(description = "공지사항 제목", example = "새로운 업데이트 안내")
        String title,
        @Schema(description = "공지사항 내용", example = "새로운 기능이 추가되었습니다.")
        String content,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/banner.jpg")
        String bannerImageUrl
) {

    public static QueryAllAnnouncementResponse from(Announcement announcement) {
        return new QueryAllAnnouncementResponse(
                announcement.id(),
                announcement.title(),
                announcement.content(),
                announcement.bannerImageUrl()
        );
    }
}
