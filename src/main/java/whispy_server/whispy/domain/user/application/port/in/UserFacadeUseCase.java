package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사용자 파사드 유스케이스.
 * 다른 도메인에서 사용자 정보를 조회할 수 있도록 제공하는 파사드 인터페이스입니다.
 */
@UseCase
public interface UserFacadeUseCase {

    /**
     * 현재 인증된 사용자를 조회합니다.
     *
     * @return 현재 인증된 사용자 도메인 객체
     */
    User currentUser();

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 조회할 이메일 주소
     * @return 사용자 도메인 객체
     */
    User getUserByEmail(String email);


}
