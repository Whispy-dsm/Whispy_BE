package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UserLoginUseCase {

    TokenResponse login(UserLoginRequest  userLoginRequest);
}
