package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyAccountInfoResponse;
import whispy_server.whispy.domain.user.application.port.in.GetMyAccountInfoUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

/**
 * 내 계정 정보 조회 서비스.
 * 현재 인증된 사용자의 계정 상세 정보를 조회합니다.
 */
@Service
@RequiredArgsConstructor
public class GetMyAccountInfoService implements GetMyAccountInfoUseCase {

    private static final String DEFAULT_PROVIDER = "일반 로그인";

    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 인증된 사용자의 계정 정보를 조회합니다.
     * 로컬 계정인 경우 비밀번호를 마스킹하여 반환합니다.
     *
     * @return 계정 정보 (이메일, 이름, 프로필 이미지, 성별, OAuth 제공자, 마스킹된 비밀번호)
     */
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
