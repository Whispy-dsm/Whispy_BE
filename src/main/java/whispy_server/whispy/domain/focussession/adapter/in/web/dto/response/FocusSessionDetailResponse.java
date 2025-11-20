package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

@Schema(description = "집중 세션 상세 응답")
public record FocusSessionDetailResponse(
        @Schema(description = "지속 시간(분)", example = "60")
        int durationMinutes,
        @Schema(description = "집중 태그")
        FocusTag tag,
        @Schema(description = "일시", example = "2024-01-01T10:00:00")
        LocalDateTime date
) {
    public static FocusSessionDetailResponse from(FocusSession focusSession) {
        return new FocusSessionDetailResponse(
                focusSession.durationSeconds() / 60,
                focusSession.tag(),
                focusSession.startedAt()
        );
    }
}
