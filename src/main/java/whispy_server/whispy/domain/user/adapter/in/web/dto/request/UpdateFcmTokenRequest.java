package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FCM 토큰 업데이트 요청")
public record UpdateFcmTokenRequest(
        @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...")
        String fcmToken
) {}
