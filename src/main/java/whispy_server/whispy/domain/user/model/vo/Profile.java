package whispy_server.whispy.domain.user.model.vo;


import whispy_server.whispy.domain.user.model.types.Gender;

public record Profile(
        String name,
        String profileImageUrl,
        Gender gender
) {
}
