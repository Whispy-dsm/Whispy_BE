package whispy_server.whispy.domain.admin.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 관리자 로그인 요청 DTO
 * <p>
 * 헥사고날 아키텍처의 인바운드 어댑터 계층에서 사용되는 요청 객체입니다.
 * 관리자 인증을 위한 로그인 정보를 전달합니다.
 * </p>
 *
 * @param adminId 관리자 로그인 ID
 * @param password 평문 비밀번호 (서버에서 암호화된 비밀번호와 비교됨)
 */
@Schema(description = "관리자 로그인 요청")
public record AdminLoginRequest(
        @Schema(description = "관리자 ID", example = "admin")
        String adminId,
        @Schema(description = "비밀번호", example = "adminpass123")
        String password
) {
}
