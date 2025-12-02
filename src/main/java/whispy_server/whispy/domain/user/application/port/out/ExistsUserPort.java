package whispy_server.whispy.domain.user.application.port.out;

/**
 * 사용자 존재 확인 아웃바운드 포트.
 * 사용자 존재 여부 확인 작업을 정의합니다.
 */
public interface ExistsUserPort {

    /**
     * 이메일로 사용자 존재 여부를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 사용자 존재 여부
     */
    boolean existsByEmail(String email);
}
