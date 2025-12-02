package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

/**
 * 명상 세션 저장 요청 DTO.
 *
 * 새로운 명상 세션을 저장하기 위한 요청 데이터를 담고 있습니다.
 */
@Schema(description = "명상 세션 저장 요청")
public record SaveMeditationSessionRequest(
        /**
         * 명상 시작 일시.
         * 예: 2024-01-01T10:00:00
         */
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        LocalDateTime startedAt,
        /**
         * 명상 종료 일시.
         * 예: 2024-01-01T10:30:00
         */
        @Schema(description = "종료 일시", example = "2024-01-01T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        LocalDateTime endedAt,
        /**
         * 명상 지속 시간(초 단위).
         * 1초 이상이어야 합니다.
         * 예: 1800 (30분)
         */
        @Schema(description = "지속 시간(초)", example = "1800", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1)
        int durationSeconds,
        /**
         * 명상 시 사용한 호흡 모드.
         * 빠른 호흡, 4-7-8 호흡, 교대 호흡, 박스 호흡, 복식 호흡 등으로 분류합니다.
         */
        @Schema(description = "호흡 모드", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        BreatheMode breatheMode
) { }
