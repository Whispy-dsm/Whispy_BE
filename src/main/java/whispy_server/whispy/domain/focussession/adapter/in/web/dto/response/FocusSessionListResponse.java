package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

@Schema(description = "집중 세션 목록 응답")
public record FocusSessionListResponse(
        @Schema(description = "세션 ID", example = "1")
        Long id,
        @Schema(description = "지속 시간(분)", example = "60")
        int durationMinutes,
        @Schema(description = "집중 태그")
        FocusTag tag,
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt

) {
    public static FocusSessionListResponse from(FocusSession focusSession) {
        return new FocusSessionListResponse(
                focusSession.id(),
                focusSession.durationSeconds() / 60,
                focusSession.tag(),
                focusSession.startedAt()
        );
    }
}
