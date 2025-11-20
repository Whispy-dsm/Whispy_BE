package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

@Schema(description = "집중 세션 응답")
public record FocusSessionResponse(
        @Schema(description = "세션 ID", example = "1")
        Long id,
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-01T11:00:00")
        LocalDateTime endedAt,
        @Schema(description = "지속 시간(초)", example = "3600")
        int durationSeconds,
        @Schema(description = "집중 태그")
        FocusTag tag,
        @Schema(description = "생성 일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "오늘 총 집중 시간(분)", example = "120")
        int todayTotalMinutes
) {
    public static FocusSessionResponse from(FocusSession focusSession, int todayTotalMinutes) {
        return new FocusSessionResponse(
                focusSession.id(),
                focusSession.userId(),
                focusSession.startedAt(),
                focusSession.endedAt(),
                focusSession.durationSeconds(),
                focusSession.tag(),
                focusSession.createdAt(),
                todayTotalMinutes
        );
    }
}
