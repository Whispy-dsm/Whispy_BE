package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 재발급 요청 DTO.
 * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.
 *
 * @param refreshToken 리프레시 토큰
 */
@Schema(description = "토큰 재발급 요청")
public record TokenReissueRequest(
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String refreshToken
) {
    @Override
    public String toString() {
        return "TokenReissueRequest[refreshToken=***]";
    }
}
