package whispy_server.whispy.domain.user.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.UserLoginUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserRegisterUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserTokenReissueUseCase;
import whispy_server.whispy.global.document.api.user.UserApiDocument;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApiDocument {

    private final UserLoginUseCase userLoginUseCase;
    private final UserRegisterUseCase userRegisterUseCase;
    private final UserTokenReissueUseCase userTokenReissueUseCase;

    @PostMapping("/login")
    public TokenResponse login(@Valid  @RequestBody UserLoginRequest request) {
        return userLoginUseCase.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        userRegisterUseCase.register(request);
    }

    @PutMapping("/reissue")
    public TokenResponse reissue(@RequestHeader("X-Refresh-Token") String token) {
        return userTokenReissueUseCase.reissue(token);
    }




}
