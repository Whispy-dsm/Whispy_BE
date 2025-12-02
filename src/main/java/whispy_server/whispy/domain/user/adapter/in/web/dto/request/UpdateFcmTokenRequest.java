package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FCM 토큰 업데이트 요청 DTO.
 * 푸시 알림을 위한 Firebase Cloud Messaging 토큰을 업데이트할 때 사용됩니다.
 *
 * @param fcmToken Firebase Cloud Messaging 토큰
 */
@Schema(description = "FCM 토큰 업데이트 요청")
public record UpdateFcmTokenRequest(
        @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...")
        String fcmToken
) {}
