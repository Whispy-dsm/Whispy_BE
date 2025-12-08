package whispy_server.whispy.domain.focussession.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

/**
 * 집중 세션 저장 요청 DTO.
 *
 * 새로운 집중 세션을 저장하기 위한 요청 데이터를 담고 있습니다.
 */
@Schema(description = "집중 세션 저장 요청")
public record SaveFocusSessionRequest(
        /**
         * 집중 시작 일시.
         * 예: 2024-01-01T10:00:00
         */
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime startedAt,
        /**
         * 집중 종료 일시.
         * 예: 2024-01-01T11:00:00
         */
        @Schema(description = "종료 일시", example = "2024-01-01T11:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime endedAt,
        /**
         * 집중 지속 시간(초 단위).
         * 1초 이상이어야 합니다.
         * 예: 3600 (1시간)
         */
        @Schema(description = "지속 시간(초)", example = "3600", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1) int durationSeconds,
        /**
         * 집중 활동 태그.
         * 업무, 공부, 독서, 명상, 스포츠 등으로 분류합니다.
         */
        @Schema(description = "집중 태그", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull FocusTag tag
) { }