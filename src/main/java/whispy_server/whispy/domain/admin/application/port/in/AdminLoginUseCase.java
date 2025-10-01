package whispy_server.whispy.domain.admin.application.port.in;

import whispy_server.whispy.domain.admin.adapter.in.web.dto.request.AdminLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;

public interface AdminLoginUseCase {
    TokenResponse execute(AdminLoginRequest request);
}
