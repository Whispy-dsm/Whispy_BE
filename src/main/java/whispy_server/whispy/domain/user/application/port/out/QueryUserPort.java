package whispy_server.whispy.domain.user.application.port.out;

import whispy_server.whispy.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface QueryUserPort {

    Optional<User> findByEmail(String email);

    List<User> findUserAll();

}




