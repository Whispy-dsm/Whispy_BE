package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import whispy_server.whispy.domain.user.model.types.Gender;

public record ChangeProfileRequest(
        @Size(min = 1, max = 30)
        @NotBlank
        String name,
        @NotBlank
        String profileImageUrl,
        @NotNull
        Gender gender
) {
}
