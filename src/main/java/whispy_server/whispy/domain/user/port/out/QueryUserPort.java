package whispy_server.whispy.domain.user.port.out;

import whispy_server.whispy.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface QueryUserPort {

    Optional<User> findByEmail(String email);

    List<User> findUserAll();

}




