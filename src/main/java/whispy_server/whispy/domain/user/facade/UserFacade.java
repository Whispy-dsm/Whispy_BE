package whispy_server.whispy.domain.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.domain.User;
import whispy_server.whispy.domain.user.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.port.out.QueryUserPort;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;


@Component
@RequiredArgsConstructor
public class UserFacade implements UserFacadeUseCase {

    private final QueryUserPort queryUserPort;

    @Override
    public User currentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = queryUserPort.findByEmail(email);
        if(user == null){
            throw UserNotFoundException.EXCEPTION;
        }
            return user;
    }
}
