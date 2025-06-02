package whispy_server.whispy.domain.user.port.out;

import whispy_server.whispy.domain.user.domain.User;

public interface UserSavePort {

    User save(User user);
}
