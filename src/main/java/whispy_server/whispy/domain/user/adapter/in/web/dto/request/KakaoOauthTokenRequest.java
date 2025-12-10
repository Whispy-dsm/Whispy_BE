package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 카카오 OAuth 인증 요청 DTO.
 * 카카오 로그인 후 발급받은 액세스 토큰으로 인증할 때 사용됩니다.
 *
 * @param accessToken 카카오에서 발급받은 액세스 토큰
 * @param fcmToken Firebase Cloud Messaging 토큰 (선택)
 */
@Schema(description = "카카오 OAuth 토큰 요청")
public record KakaoOauthTokenRequest(
        @Schema(description = "카카오 액세스 토큰", example = "kakao_access_token_example", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 500)
        String accessToken,
        @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...")
        @Size(max = 255)
        String fcmToken
) {
}
