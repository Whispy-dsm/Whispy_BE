package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UseCase;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

/**
 * OAuth 사용자 처리 유스케이스.
 * OAuth 인증 후 사용자를 조회하거나 신규 생성합니다.
 */
@UseCase
public interface OauthUserUseCase {

    /**
     * OAuth 사용자 정보로 기존 사용자를 조회하거나 신규 생성합니다.
     * 이메일이 존재하면 해당 사용자를 반환하고, 없으면 새로 생성합니다.
     *
     * @param oauthUserInfo OAuth에서 받은 사용자 정보
     * @param provider OAuth 제공자 (GOOGLE, KAKAO)
     * @return 조회되거나 생성된 사용자 도메인 객체
     */
    User findOrCreateOauthUser(OauthUserInfo oauthUserInfo, String provider);
}
