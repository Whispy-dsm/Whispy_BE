package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 내 프로필 조회 유스케이스.
 * 현재 인증된 사용자의 프로필 정보를 조회합니다.
 */
@UseCase
public interface GetMyProfileUseCase {
    /**
     * 현재 인증된 사용자의 프로필 정보를 조회합니다.
     *
     * @return 사용자 프로필 정보 (이름, 프로필 이미지, 가입 후 경과 일수)
     */
    MyProfileResponse execute();
}
