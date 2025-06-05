package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UserFacadeUseCase {

    User currentUser();

    User getUserByEmail(String email);


}
