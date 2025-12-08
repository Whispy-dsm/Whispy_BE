package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

/**
 * 집중 세션 목록 응답 DTO.
 *
 * 집중 세션 목록 조회 시 각 항목의 요약 정보를 클라이언트에게 반환합니다.
 */
@Schema(description = "집중 세션 목록 응답")
public record FocusSessionListResponse(
        /**
         * 집중 세션 ID.
         */
        @Schema(description = "세션 ID", example = "1")
        Long id,
        /**
         * 집중 지속 시간(분 단위).
         * 저장된 초 단위 지속 시간을 분으로 변환합니다.
         */
        @Schema(description = "지속 시간(분)", example = "60")
        int durationMinutes,
        /**
         * 집중 활동의 분류 태그.
         */
        @Schema(description = "집중 태그")
        FocusTag tag,
        /**
         * 집중 시작 일시.
         */
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt

) {
    /**
     * 도메인 모델로부터 목록 응답 DTO를 생성합니다.
     *
     * @param focusSession 변환할 집중 세션 도메인 모델
     * @return 생성된 목록 응답 DTO
     */
    public static FocusSessionListResponse from(FocusSession focusSession) {
        return new FocusSessionListResponse(
                focusSession.id(),
                focusSession.durationSeconds() / 60,
                focusSession.tag(),
                focusSession.startedAt()
        );
    }
}
