package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyAccountInfoResponse;
import whispy_server.whispy.domain.user.application.port.in.GetMyAccountInfoUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class GetMyAccountInfoService implements GetMyAccountInfoUseCase {

    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public MyAccountInfoResponse execute() {
        User currentUser = userFacadeUseCase.currentUser();
        return MyAccountInfoResponse.from(currentUser);
    }
}
