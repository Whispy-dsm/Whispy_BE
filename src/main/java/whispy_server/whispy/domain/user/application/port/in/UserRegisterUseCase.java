package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UserRegisterUseCase {

    void register(RegisterRequest registerRequest);
}
