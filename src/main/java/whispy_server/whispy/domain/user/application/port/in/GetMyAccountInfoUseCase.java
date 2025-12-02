package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyAccountInfoResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 내 계정 정보 조회 유스케이스.
 * 현재 인증된 사용자의 계정 상세 정보를 조회합니다.
 */
@UseCase
public interface GetMyAccountInfoUseCase {
    /**
     * 현재 인증된 사용자의 계정 정보를 조회합니다.
     *
     * @return 계정 정보 (이메일, 이름, 프로필 이미지, 성별, OAuth 제공자, 마스킹된 비밀번호)
     */
    MyAccountInfoResponse execute();
}
