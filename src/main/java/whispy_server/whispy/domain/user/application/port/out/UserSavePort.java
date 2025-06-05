package whispy_server.whispy.domain.user.application.port.out;

import whispy_server.whispy.domain.user.model.User;

public interface UserSavePort {

    void save(User user);
}
