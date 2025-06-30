package whispy_server.whispy.domain.user.application.port.out;

public interface ExistsUserPort {

    boolean existsByEmail(String email);
}
