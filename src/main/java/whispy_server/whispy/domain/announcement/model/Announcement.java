package whispy_server.whispy.domain.announcement.model;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

/**
 * 공지사항 도메인 모델.
 *
 * DDD의 Aggregate Root로 공지사항 관련 비즈니스 로직을 캡슐화합니다.
 * 공지사항 내용은 마크다운 형식으로 작성되며, 이미지는 마크다운 문법으로 포함할 수 있습니다.
 *
 * @param id 공지사항 ID
 * @param title 공지사항 제목
 * @param content 공지사항 내용 (마크다운 형식)
 * @param createdAt 생성 일시
 */
@Aggregate
public record Announcement(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt
) {
    /**
     * 공지사항 정보를 수정합니다.
     *
     * @param request 수정할 정보가 포함된 요청 (null 값은 기존 값 유지)
     * @return 수정된 공지사항 객체
     */
    public Announcement update(UpdateAnnouncementRequest request) {
        return new Announcement(
                this.id,
                request.title() != null ? request.title() : this.title,
                request.content() != null ? request.content() : this.content,
                this.createdAt
        );
    }
}
