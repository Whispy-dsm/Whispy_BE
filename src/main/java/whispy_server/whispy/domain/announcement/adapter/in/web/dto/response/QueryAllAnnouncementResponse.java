package whispy_server.whispy.domain.announcement.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.announcement.model.Announcement;

/**
 * 전체 공지사항 조회 응답 DTO.
 *
 * 공지사항 목록 조회 시 반환되는 응답 데이터입니다.
 *
 * @param id 공지사항 ID
 * @param title 공지사항 제목
 * @param content 공지사항 내용
 * @param bannerImageUrl 배너 이미지 URL
 */
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

    /**
     * Announcement 도메인 모델을 응답 DTO로 변환합니다.
     *
     * @param announcement 공지사항 도메인 모델
     * @return 전체 공지사항 조회 응답 DTO
     */
    public static QueryAllAnnouncementResponse from(Announcement announcement) {
        return new QueryAllAnnouncementResponse(
                announcement.id(),
                announcement.title(),
                announcement.content(),
                announcement.bannerImageUrl()
        );
    }
}
