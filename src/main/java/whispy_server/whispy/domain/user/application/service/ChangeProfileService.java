package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangeProfileRequest;
import whispy_server.whispy.domain.user.application.port.in.ChangeProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 프로필 변경 서비스.
 * 사용자의 프로필 정보(이름, 이미지, 성별)를 변경합니다.
 */
@Service
@RequiredArgsConstructor
public class ChangeProfileService implements ChangeProfileUseCase {

    private final UserFacadeUseCase userFacadeUseCase;
    private final UserSavePort userSavePort;

    /**
     * 사용자의 프로필 정보를 변경합니다.
     *
     * @param request 프로필 변경 요청 (이름, 프로필 이미지 URL, 성별)
     */
    @Override
    @Transactional
    @UserAction("프로필 변경")
    public void execute(ChangeProfileRequest request) {
        User user = userFacadeUseCase.currentUser();

        User updateUser = user.changeProfile(request.name(), request.profileImageUrl(), request.gender());
        userSavePort.save(updateUser);

    }
}
