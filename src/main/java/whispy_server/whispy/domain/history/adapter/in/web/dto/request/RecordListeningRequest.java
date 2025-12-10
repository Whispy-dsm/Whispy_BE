package whispy_server.whispy.domain.history.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 청취 기록 요청 DTO.
 */
@Schema(description = "청취 기록 요청")
public record RecordListeningRequest(
        @Schema(description = "음악 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long musicId
) {
}
