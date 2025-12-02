package whispy_server.whispy.domain.auth.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 이메일 인증 상태 확인 응답 DTO
 *
 * 이메일 인증 상태 조회 결과를 클라이언트에 반환합니다.
 * Redis에 저장된 인증 상태 값을 기반으로 인증 완료 여부를 제공합니다.
 */
@Schema(description = "이메일 인증 상태 확인 응답")
public record CheckEmailVerificationResponse(
        @Schema(description = "인증 완료 여부", example = "true")
        boolean isVerified
) {}
