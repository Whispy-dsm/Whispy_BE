package whispy_server.whispy.domain.user.domain.vo;


import whispy_server.whispy.domain.user.domain.types.Gender;

public record Profile(
        String name,
        String profileImageUrl,
        Gender gender
) {
}
