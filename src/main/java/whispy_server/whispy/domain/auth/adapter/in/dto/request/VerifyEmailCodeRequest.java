package whispy_server.whispy.domain.auth.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 이메일 인증 코드 검증 요청 DTO
 *
 * 사용자가 입력한 6자리 인증 코드를 검증하기 위한 요청 데이터를 담고 있습니다.
 * Redis에 저장된 인증 코드와 일치 여부를 확인하고, 성공 시 인증 상태를 저장합니다.
 */
@Schema(description = "이메일 인증 코드 검증 요청")
public record VerifyEmailCodeRequest(
        @Schema(description = "인증할 이메일 주소", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @Schema(description = "6자리 인증 코드", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "인증 코드는 필수입니다.")
        @Pattern(regexp = "^\\d{6}$", message = "인증 코드는 6자리 숫자여야 합니다.")
        String code
) {
    @Override
    public String toString() {
        return "VerifyEmailCodeRequest[email=***, code=***]";
    }
}
