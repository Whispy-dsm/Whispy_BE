package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

/**
 * 사용자 구독 정보 조회 응답 DTO.
 *
 * 구매 토큰을 제외한 요약 정보만 반환한다.
 *
 * @param subscriptions 구독 요약 정보
 */
@Schema(description = "사용자 구독 정보 조회 응답")
public record GetUserSubscriptionsResponse(
        @Schema(description = "구독 요약 정보")
        Optional<SubscriptionSummaryResponse> subscriptions
) {
}
