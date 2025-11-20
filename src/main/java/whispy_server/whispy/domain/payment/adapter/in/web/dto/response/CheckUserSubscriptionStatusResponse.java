package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 구독 상태 확인 응답")
public record CheckUserSubscriptionStatusResponse(
        @Schema(description = "구독 여부", example = "true")
        boolean isSubscribed
) {}
