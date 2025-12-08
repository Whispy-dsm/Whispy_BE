package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

/**
 * 수면 세션 응답 DTO.
 *
 * 수면 세션 저장 후 클라이언트에게 반환되는 응답 데이터입니다.
 */
@Schema(description = "수면 세션 응답")
public record SleepSessionResponse(
        /**
         * 수면 세션 ID.
         */
        @Schema(description = "세션 ID", example = "1")
        Long id,
        /**
         * 세션을 기록한 사용자 ID.
         */
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        /**
         * 수면 시작 일시.
         */
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00")
        LocalDateTime startedAt,
        /**
         * 수면 종료 일시.
         */
        @Schema(description = "종료 일시", example = "2024-01-02T06:00:00")
        LocalDateTime endedAt,
        /**
         * 수면 지속 시간(초 단위).
         */
        @Schema(description = "지속 시간(초)", example = "28800")
        int durationSeconds,
        /**
         * 세션 생성 일시.
         */
        @Schema(description = "생성 일시", example = "2024-01-01T22:00:00")
        LocalDateTime createdAt
) {
    /**
     * 도메인 모델로부터 응답 DTO를 생성합니다.
     *
     * @param sleepSession 변환할 수면 세션 도메인 모델
     * @return 생성된 응답 DTO
     */
    public static SleepSessionResponse from(SleepSession sleepSession) {
        return new SleepSessionResponse(
                sleepSession.id(),
                sleepSession.userId(),
                sleepSession.startedAt(),
                sleepSession.endedAt(),
                sleepSession.durationSeconds(),
                sleepSession.createdAt()
        );
    }
}
