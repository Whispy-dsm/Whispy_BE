package whispy_server.whispy.domain.admin.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

/**
 * 관리자 로그인 요청 DTO
 *
 * 헥사고날 아키텍처의 인바운드 어댑터 계층에서 사용되는 요청 객체입니다.
 * 관리자 인증을 위한 로그인 정보를 전달합니다.
 *
 * @param adminId 관리자 로그인 ID
 * @param password 평문 비밀번호 (서버에서 암호화된 비밀번호와 비교됨)
 */
@ToString(exclude = {"password"})
@Schema(description = "관리자 로그인 요청")
public record AdminLoginRequest(
        @Schema(description = "관리자 ID", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String adminId,
        @Schema(description = "비밀번호", example = "adminpass123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min = 8, max = 70)
        String password
) {
}
