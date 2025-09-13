package whispy_server.whispy.domain.user.application.port.out;

import whispy_server.whispy.domain.user.model.User;

import java.util.Optional;

public interface QueryUserPort {

    Optional<User> findByEmail(String email);

}




