package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
