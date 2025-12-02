package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

/**
 * 명상 세션 응답 DTO.
 *
 * 명상 세션 저장 후 클라이언트에게 반환되는 응답 데이터입니다.
 * 저장된 세션 정보와 함께 오늘의 총 명상 시간을 포함합니다.
 */
@Schema(description = "명상 세션 응답")
public record MeditationSessionResponse(
        /**
         * 명상 세션 ID.
         */
        @Schema(description = "세션 ID", example = "1")
        Long id,
        /**
         * 세션을 기록한 사용자 ID.
         */
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        /**
         * 명상 시작 일시.
         */
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt,
        /**
         * 명상 종료 일시.
         */
        @Schema(description = "종료 일시", example = "2024-01-01T10:30:00")
        LocalDateTime endedAt,
        /**
         * 명상 지속 시간(초 단위).
         */
        @Schema(description = "지속 시간(초)", example = "1800")
        int durationSeconds,
        /**
         * 명상 시 사용한 호흡 모드.
         */
        @Schema(description = "호흡 모드")
        BreatheMode breatheMode,
        /**
         * 세션 생성 일시.
         */
        @Schema(description = "생성 일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,
        /**
         * 오늘 하루 동안의 총 명상 시간(분 단위).
         */
        @Schema(description = "오늘 총 명상 시간(분)", example = "60")
        int todayTotalMinutes
) {
    /**
     * 도메인 모델로부터 응답 DTO를 생성합니다.
     *
     * @param meditationSession 변환할 명상 세션 도메인 모델
     * @param todayTotalMinutes 오늘의 총 명상 시간(분 단위)
     * @return 생성된 응답 DTO
     */
    public static MeditationSessionResponse from(MeditationSession meditationSession, int todayTotalMinutes) {
        return new MeditationSessionResponse(
                meditationSession.id(),
                meditationSession.userId(),
                meditationSession.startedAt(),
                meditationSession.endedAt(),
                meditationSession.durationSeconds(),
                meditationSession.breatheMode(),
                meditationSession.createdAt(),
                todayTotalMinutes
        );
    }
}
