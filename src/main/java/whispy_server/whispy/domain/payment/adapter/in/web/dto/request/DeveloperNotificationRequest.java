package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 개발자 알림 요청 DTO.
 *
 * Google Play에서 전송하는 개발자 알림의 최상위 구조입니다.
 *
 * @param version API 버전
 * @param packageName 패키지 이름
 * @param eventTimeMillis 이벤트 시간 (밀리초)
 * @param subscriptionNotification 구독 알림 정보
 * @param oneTimeProductNotification 일회성 상품 알림 정보
 */
@Schema(description = "개발자 알림 요청")
public record DeveloperNotificationRequest(
        @Schema(description = "API 버전", example = "1.0")
        String version,
        @Schema(description = "패키지 이름", example = "com.example.app")
        String packageName,
        @Schema(description = "이벤트 시간 (밀리초)", example = "1638360000000")
        Long eventTimeMillis,
        @Schema(description = "구독 알림 정보")
        SubscriptionNotificationRequest subscriptionNotification,
        @Schema(description = "일회성 상품 알림 정보")
        OneTimeProductNotificationRequest oneTimeProductNotification
) {}
