package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import whispy_server.whispy.domain.user.model.User;

public record MyProfileResponse(
        String name,
        String profileImageUrl,
        Long daysSinceRegistration
) {
    public static MyProfileResponse from(User user) {
        return new MyProfileResponse(
                user.name(),
                user.profileImageUrl(),
                user.getDaysSinceRegistration()
        );
    }
}
