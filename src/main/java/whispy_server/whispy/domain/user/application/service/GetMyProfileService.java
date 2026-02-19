package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.domain.user.application.port.in.GetMyProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.config.redis.RedisConfig;

/**
 * 내 프로필 조회 서비스.
 * 현재 인증된 사용자의 프로필 정보를 조회합니다.
 */
@Service
@RequiredArgsConstructor
public class GetMyProfileService implements GetMyProfileUseCase {

    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 인증된 사용자의 프로필 정보를 조회합니다.
     *
     * @return 사용자 프로필 정보 (이름, 프로필 이미지, 가입 후 경과 일수)
     */
    @Override
    @UserAction("내 프로필 조회")
    @Cacheable(
            value = RedisConfig.USER_MY_PROFILE_CACHE,
            keyGenerator = "userProfileKeyGenerator",
            sync = true
    )
    public MyProfileResponse execute() {
        User currentUser = userFacadeUseCase.currentUser();
        return MyProfileResponse.from(currentUser);
    }
}
