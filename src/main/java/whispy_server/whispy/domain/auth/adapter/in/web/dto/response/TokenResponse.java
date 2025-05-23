package whispy_server.whispy.domain.auth.adapter.in.web.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
