package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

/**
 * 수면 세션 목록 응답 DTO.
 *
 * 수면 세션 목록 조회 시 각 항목의 요약 정보를 클라이언트에게 반환합니다.
 */
@Schema(description = "수면 세션 목록 응답")
public record SleepSessionListResponse(
        /**
         * 수면 세션 ID.
         */
        @Schema(description = "세션 ID", example = "1")
        Long id,
        /**
         * 수면 시작 일시.
         */
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00")
        LocalDateTime startedAt,
        /**
         * 수면 지속 시간(분 단위).
         * 저장된 초 단위 지속 시간을 분으로 변환합니다.
         */
        @Schema(description = "지속 시간(분)", example = "480")
        int durationMinutes
) {
    /**
     * 도메인 모델로부터 목록 응답 DTO를 생성합니다.
     *
     * @param sleepSession 변환할 수면 세션 도메인 모델
     * @return 생성된 목록 응답 DTO
     */
    public static SleepSessionListResponse from(SleepSession sleepSession) {
        return new SleepSessionListResponse(
                sleepSession.id(),
                sleepSession.startedAt(),
                sleepSession.durationSeconds() / 60
        );
    }

}
