package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequest(

    @Email
    String email,
    @NotBlank
    String password,
    @NotBlank
    String fcmToken
) {
}
