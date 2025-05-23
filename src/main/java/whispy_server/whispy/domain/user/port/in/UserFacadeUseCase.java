package whispy_server.whispy.domain.user.port.in;

import whispy_server.whispy.domain.user.domain.User;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UserFacadeUseCase {

    User currentUser();

    User getUserByEmail(String email);


}
