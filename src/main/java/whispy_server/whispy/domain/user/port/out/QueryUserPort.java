package whispy_server.whispy.domain.user.port.out;

import whispy_server.whispy.domain.user.domain.User;

import java.util.List;

public interface QueryUserPort {

    User findByEmail(String email);

    List<User> userAll();



}




