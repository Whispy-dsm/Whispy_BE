package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.domain.user.application.port.in.GetMyProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class GetMyProfileService implements GetMyProfileUseCase {

    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public MyProfileResponse execute() {
        User currentUser = userFacadeUseCase.currentUser();
        return MyProfileResponse.from(currentUser);
    }
}
