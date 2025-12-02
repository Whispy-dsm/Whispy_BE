package whispy_server.whispy.domain.user.application.port.out;

import whispy_server.whispy.domain.user.model.User;

import java.util.Optional;

/**
 * 사용자 조회 아웃바운드 포트.
 * 사용자 조회 관련 영속성 작업을 정의합니다.
 */
public interface QueryUserPort {

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 조회할 이메일 주소
     * @return 사용자 도메인 객체 (Optional)
     */
    Optional<User> findByEmail(String email);

}




