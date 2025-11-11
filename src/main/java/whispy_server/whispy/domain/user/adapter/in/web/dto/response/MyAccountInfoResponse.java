package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;

public record MyAccountInfoResponse(
        String name,
        String profileImageUrl,
        String email,
        Gender gender,
        String provider
) {
    public static MyAccountInfoResponse from(User user) {
        return new MyAccountInfoResponse(
                user.name(),
                user.profileImageUrl(),
                user.email(),
                user.gender(),
                user.provider()
        );
    }
}
