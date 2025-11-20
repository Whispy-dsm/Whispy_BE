package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카카오 OAuth 토큰 요청")
public record KakaoOauthTokenRequest(
        @Schema(description = "카카오 액세스 토큰", example = "kakao_access_token_example", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String accessToken,
        @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...")
        String fcmToken
) {
}
