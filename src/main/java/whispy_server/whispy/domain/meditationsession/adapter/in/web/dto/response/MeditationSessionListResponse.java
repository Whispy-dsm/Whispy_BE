package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

/**
 * 명상 세션 목록 응답 DTO.
 *
 * 명상 세션 목록 조회 시 각 항목의 요약 정보를 클라이언트에게 반환합니다.
 */
@Schema(description = "명상 세션 목록 응답")
public record MeditationSessionListResponse(
        /**
         * 명상 세션 ID.
         */
        @Schema(description = "세션 ID", example = "1")
        Long id,
        /**
         * 명상 지속 시간(분 단위).
         * 저장된 초 단위 지속 시간을 분으로 변환합니다.
         */
        @Schema(description = "지속 시간(분)", example = "30")
        int durationMinutes,
        /**
         * 명상 시 사용한 호흡 모드.
         */
        @Schema(description = "호흡 모드")
        BreatheMode breatheMode,
        /**
         * 명상 시작 일시.
         */
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt

) {
    /**
     * 도메인 모델로부터 목록 응답 DTO를 생성합니다.
     *
     * @param meditationSession 변환할 명상 세션 도메인 모델
     * @return 생성된 목록 응답 DTO
     */
    public static MeditationSessionListResponse from(MeditationSession meditationSession) {
        return new MeditationSessionListResponse(
                meditationSession.id(),
                meditationSession.durationSeconds() / 60,
                meditationSession.breatheMode(),
                meditationSession.startedAt()
        );
    }
}
