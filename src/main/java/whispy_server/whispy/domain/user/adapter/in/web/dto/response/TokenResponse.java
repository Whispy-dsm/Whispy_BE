package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JWT 토큰 응답 DTO.
 * 로그인, OAuth 인증, 토큰 재발급 시 반환됩니다.
 *
 * @param accessToken JWT 액세스 토큰 (API 요청에 사용)
 * @param refreshToken JWT 리프레시 토큰 (액세스 토큰 재발급에 사용)
 */
@Schema(description = "토큰 응답")
public record TokenResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken
) {
}
