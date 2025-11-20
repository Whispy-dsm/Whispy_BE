package whispy_server.whispy.domain.auth.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증 상태 확인 응답")
public record CheckEmailVerificationResponse(
        @Schema(description = "인증 완료 여부", example = "true")
        boolean isVerified
) {}
