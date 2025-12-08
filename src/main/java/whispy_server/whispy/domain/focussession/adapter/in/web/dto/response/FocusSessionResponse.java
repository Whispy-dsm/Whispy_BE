package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

/**
 * 집중 세션 응답 DTO.
 *
 * 집중 세션 저장 후 클라이언트에게 반환되는 응답 데이터입니다.
 * 저장된 세션 정보와 함께 오늘의 총 집중 시간을 포함합니다.
 */
@Schema(description = "집중 세션 응답")
public record FocusSessionResponse(
        /**
         * 집중 세션 ID.
         */
        @Schema(description = "세션 ID", example = "1")
        Long id,
        /**
         * 세션을 기록한 사용자 ID.
         */
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        /**
         * 집중 시작 일시.
         */
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt,
        /**
         * 집중 종료 일시.
         */
        @Schema(description = "종료 일시", example = "2024-01-01T11:00:00")
        LocalDateTime endedAt,
        /**
         * 집중 지속 시간(초 단위).
         */
        @Schema(description = "지속 시간(초)", example = "3600")
        int durationSeconds,
        /**
         * 집중 활동의 분류 태그.
         */
        @Schema(description = "집중 태그")
        FocusTag tag,
        /**
         * 세션 생성 일시.
         */
        @Schema(description = "생성 일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,
        /**
         * 오늘 하루 동안의 총 집중 시간(분 단위).
         */
        @Schema(description = "오늘 총 집중 시간(분)", example = "120")
        int todayTotalMinutes
) {
    /**
     * 도메인 모델로부터 응답 DTO를 생성합니다.
     *
     * @param focusSession 변환할 집중 세션 도메인 모델
     * @param todayTotalMinutes 오늘의 총 집중 시간(분 단위)
     * @return 생성된 응답 DTO
     */
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
