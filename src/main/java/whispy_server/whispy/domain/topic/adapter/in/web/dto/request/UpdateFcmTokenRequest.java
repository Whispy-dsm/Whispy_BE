package whispy_server.whispy.domain.topic.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 디바이스의 최신 FCM 토큰을 전달받기 위한 요청 DTO.
 */
@Schema(description = "FCM 토큰 업데이트 요청")
public record UpdateFcmTokenRequest(
        @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...")
        String fcmToken
) {}
