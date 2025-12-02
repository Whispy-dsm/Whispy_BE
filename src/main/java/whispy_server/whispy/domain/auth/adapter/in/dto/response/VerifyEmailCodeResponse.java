package whispy_server.whispy.domain.auth.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 이메일 인증 코드 검증 응답 DTO
 *
 * 이메일 인증 코드 검증 결과를 클라이언트에 반환합니다.
 * 인증 코드가 유효하고 일치하면 true, 그렇지 않으면 false를 반환합니다.
 */
@Schema(description = "이메일 인증 코드 검증 응답")
public record VerifyEmailCodeResponse(
        @Schema(description = "인증 성공 여부", example = "true")
        boolean isVerified
) {}
