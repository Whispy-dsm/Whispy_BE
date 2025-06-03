package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequest(

    @Email
    String email,
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "비밀번호는 8자 이상, 숫자 1개 이상, 특수문자 1개 이상 포함해야 합니다.")
    String password
) {
}
