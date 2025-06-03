package whispy_server.whispy.domain.user.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UserTokenReissueUseCase {

    TokenResponse reissue(String token);
}
