package whispy_server.whispy.domain.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserLoginUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {

    private final JwtTokenProvider  jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final QueryUserPort queryUserPort;

    @Transactional
    @Override
    public TokenResponse login(UserLoginRequest request){
        authenticatedUser(request);
        return generateToken(request.email());
    }

    private void authenticatedUser(UserLoginRequest request){
        User user = queryUserPort.findByEmail(request.email())
                .orElseThrow(()-> UserNotFoundException.EXCEPTION);

        if(!passwordEncoder.matches(request.password(), user.password())){
                throw PasswordMissMatchException.EXCEPTION;
        }
    }

    private TokenResponse generateToken(String email){
        return jwtTokenProvider.generateToken(email, Role.USER.toString());
    }
}
