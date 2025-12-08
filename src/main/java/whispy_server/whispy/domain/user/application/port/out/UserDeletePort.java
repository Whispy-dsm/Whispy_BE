package whispy_server.whispy.domain.user.application.port.out;

/**
 * 사용자 삭제 아웃바운드 포트.
 * 사용자 삭제 관련 영속성 작업을 정의합니다.
 */
public interface UserDeletePort {

    /**
     * ID로 사용자를 삭제합니다.
     *
     * @param id 삭제할 사용자 ID
     */
    void deleteById(Long id);
}
