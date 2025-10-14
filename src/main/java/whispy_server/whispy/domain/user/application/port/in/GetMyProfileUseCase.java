package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetMyProfileUseCase {
    MyProfileResponse execute();
}
