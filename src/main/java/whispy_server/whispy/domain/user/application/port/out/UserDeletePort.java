package whispy_server.whispy.domain.user.application.port.out;

import java.util.UUID;

public interface UserDeletePort {

    void deleteByUUID(UUID id);
}
