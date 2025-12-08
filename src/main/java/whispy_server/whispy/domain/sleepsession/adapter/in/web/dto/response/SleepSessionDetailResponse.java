package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

/**
 * 수면 세션 상세 응답 DTO.
 *
 * 특정 수면 세션의 상세 정보를 클라이언트에게 반환합니다.
 * 지속 시간은 분 단위로 제공됩니다.
 */
@Schema(description = "수면 세션 상세 응답")
public record SleepSessionDetailResponse(
        /**
         * 수면 지속 시간(분 단위).
         * 저장된 초 단위 지속 시간을 분으로 변환합니다.
         */
        @Schema(description = "지속 시간(분)", example = "480")
        int durationMinutes,
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
         * 세션 생성 일시.
         */
        @Schema(description = "생성 일시", example = "2024-01-01T22:00:00")
        LocalDateTime createAt
) {
    /**
     * 도메인 모델로부터 상세 응답 DTO를 생성합니다.
     *
     * @param sleepSession 변환할 수면 세션 도메인 모델
     * @return 생성된 상세 응답 DTO
     */
    public static SleepSessionDetailResponse from(SleepSession sleepSession) {
        return new SleepSessionDetailResponse(
                sleepSession.durationSeconds() / 60,
                sleepSession.startedAt(),
                sleepSession.endedAt(),
                sleepSession.createdAt()
        );
    }
}
