package whispy_server.whispy.domain.auth.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 이메일 인증 코드 발송 요청 DTO
 *
 * 사용자에게 이메일로 6자리 인증 코드를 발송하기 위한 요청 데이터를 담고 있습니다.
 * Rate limit과 중복 요청 검증을 거쳐 인증 코드가 발송됩니다.
 */
@Schema(description = "이메일 인증 코드 발송 요청")
public record SendEmailVerificationRequest(
        @Schema(description = "인증 코드를 받을 이메일 주소", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email
) {}
