package whispy_server.whispy.domain.user.application.port.out;

/**
 * 사용자 아웃바운드 포트.
 * 사용자 관련 모든 영속성 작업을 통합하는 포트 인터페이스입니다.
 * QueryUserPort, UserSavePort, ExistsUserPort, UserDeletePort를 상속합니다.
 */
public interface UserPort extends QueryUserPort, UserSavePort, ExistsUserPort, UserDeletePort {
}
