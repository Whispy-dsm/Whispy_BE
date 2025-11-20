package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

@Schema(description = "사용자 구독 정보 조회 응답")
public record GetUserSubscriptionsResponse(
        @Schema(description = "구독 정보")
        Optional<Subscription> subscriptions
) {}
