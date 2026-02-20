package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * OAuth 로그인 성공 후 앱으로 전달된 일회용 코드를 교환하기 위한 요청 DTO.
 *
 * @param code OAuth 성공 시 전달된 one-time code
 */
@Schema(description = "OAuth 코드 교환 요청")
public record OauthCodeExchangeRequest(
        @Schema(description = "OAuth 일회용 코드", example = "f0ecf4c4-4d17-4f53-bf95-d5e78d5977a8", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String code
) {
}
