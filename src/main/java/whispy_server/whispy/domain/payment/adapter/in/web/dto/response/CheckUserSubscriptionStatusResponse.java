package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 구독 상태 확인 응답 DTO.
 *
 * 사용자의 현재 구독 상태 정보를 반환합니다.
 *
 * @param isSubscribed 구독 여부
 */
@Schema(description = "사용자 구독 상태 확인 응답")
public record CheckUserSubscriptionStatusResponse(
        @Schema(description = "구독 여부", example = "true")
        boolean isSubscribed
) {}
