package whispy_server.whispy.domain.announcement.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 공지사항 조회 응답 DTO.
 *
 * 특정 공지사항 상세 조회 시 반환되는 응답 데이터입니다.
 *
 * @param title 공지사항 제목
 * @param content 공지사항 내용 (마크다운 형식)
 */
@Schema(description = "공지사항 조회 응답")
public record QueryAnnouncementResponse(
        @Schema(description = "공지사항 제목", example = "새로운 업데이트 안내")
        String title,
        @Schema(description = "공지사항 내용 (마크다운)", example = "![배너](https://example.com/banner.jpg)\n\n새로운 기능이 추가되었습니다.")
        String content
) {}
