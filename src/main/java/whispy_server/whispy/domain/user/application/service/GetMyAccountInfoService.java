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

    private static final String DEFAULT_PROVIDER = "일반 로그인";

    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public MyAccountInfoResponse execute() {
        User currentUser = userFacadeUseCase.currentUser();

        String maskedPassword = null;
        if (DEFAULT_PROVIDER.equals(currentUser.provider()) && currentUser.password() != null) {
            maskedPassword = "*".repeat(currentUser.password().length());
        }

        return MyAccountInfoResponse.of(currentUser, maskedPassword);
    }
}
