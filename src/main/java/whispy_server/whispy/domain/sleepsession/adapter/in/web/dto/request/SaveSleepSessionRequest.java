package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 수면 세션 저장 요청 DTO.
 *
 * 새로운 수면 세션을 저장하기 위한 요청 데이터를 담고 있습니다.
 */
@Schema(description = "수면 세션 저장 요청")
public record SaveSleepSessionRequest(
        /**
         * 수면 시작 일시.
         * 예: 2024-01-01T22:00:00
         */
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime startedAt,
        /**
         * 수면 종료 일시.
         * 예: 2024-01-02T06:00:00
         */
        @Schema(description = "종료 일시", example = "2024-01-02T06:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime endedAt,
        /**
         * 수면 지속 시간(초 단위).
         * 1초 이상이어야 합니다.
         * 예: 28800 (8시간)
         */
        @Schema(description = "지속 시간(초)", example = "28800", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1) int durationSeconds
) { }
