package whispy_server.whispy.domain.auth.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증 코드 검증 응답")
public record VerifyEmailCodeResponse(
        @Schema(description = "인증 성공 여부", example = "true")
        boolean isVerified
) {}
