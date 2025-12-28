package whispy_server.whispy.domain.user.application.port.out;

import whispy_server.whispy.domain.user.model.User;

/**
 * 사용자 저장 아웃바운드 포트.
 * 사용자 저장 관련 영속성 작업을 정의합니다.
 */
public interface UserSavePort {

    /**
     * 사용자를 저장합니다.
     * 신규 생성 또는 업데이트 모두 이 메서드를 사용합니다.
     *
     * @param user 저장할 사용자 도메인 객체
     * @return 저장된 사용자 도메인 객체 (ID 포함)
     */
    User save(User user);
}
