package whispy_server.whispy.domain.admin.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관리자 로그인 요청")
public record AdminLoginRequest(
        @Schema(description = "관리자 ID", example = "admin")
        String adminId,
        @Schema(description = "비밀번호", example = "adminpass123")
        String password
) {
}
