package whispy_server.whispy.domain.notification.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "읽지 않은 알림 수 응답")
public record UnreadCountResponse(
        @Schema(description = "읽지 않은 알림 개수", example = "5")
        int count
) {}
