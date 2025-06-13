package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record KakaoOauthTokenRequest(
        @NotBlank
        String accessToken
) {
}
