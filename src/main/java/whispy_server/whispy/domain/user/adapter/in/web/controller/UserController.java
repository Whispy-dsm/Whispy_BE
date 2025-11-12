package whispy_server.whispy.domain.user.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangePasswordRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangeProfileRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.KakaoOauthTokenRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ResetPasswordRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyAccountInfoResponse;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.ChangePasswordUseCase;
import whispy_server.whispy.domain.user.application.port.in.ChangeProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.GetMyAccountInfoUseCase;
import whispy_server.whispy.domain.user.application.port.in.GetMyProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.KakaoOauthUseCase;
import whispy_server.whispy.domain.user.application.port.in.ResetPasswordUseCase;
import whispy_server.whispy.domain.user.application.port.in.UpdateFcmTokenUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserLoginUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserLogoutUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserRegisterUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserTokenReissueUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserWithdrawalUseCase;
import whispy_server.whispy.global.document.api.user.UserApiDocument;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApiDocument {

    private final UserLoginUseCase userLoginUseCase;
    private final UserRegisterUseCase userRegisterUseCase;
    private final UserTokenReissueUseCase userTokenReissueUseCase;
    private final KakaoOauthUseCase kakaoOauthUseCase;
    private final UserLogoutUseCase userLogoutUseCase;
    private final UserWithdrawalUseCase userWithdrawalUseCase;
    private final UpdateFcmTokenUseCase updateFcmTokenUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final GetMyProfileUseCase getMyProfileUseCase;
    private final GetMyAccountInfoUseCase getMyAccountInfoUseCase;
    private final ChangeProfileUseCase changeProfileUseCase;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userLoginUseCase.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        userRegisterUseCase.register(request);
    }

    @PostMapping("/oauth/kakao")
    public TokenResponse authenticateWithKakaoToken(@Valid @RequestBody KakaoOauthTokenRequest request){
        return kakaoOauthUseCase.loginWithKakao(request.accessToken(), request.fcmToken());
    }

    @PutMapping("/reissue")
    public TokenResponse reissue(@RequestHeader("X-Refresh-Token") String token) {
        return userTokenReissueUseCase.reissue(token);
    }

    @PostMapping("/logout")
    public void logout(){
        userLogoutUseCase.logout();
    }

    @DeleteMapping("/withdrawal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawal(){
        userWithdrawalUseCase.withdrawal();
    }

    @PatchMapping("/fcm-token")
    public void updateFcmToken(@Valid @RequestBody UpdateFcmTokenRequest request) {
        updateFcmTokenUseCase.execute(request.fcmToken());
    }

    @PostMapping("/password/change")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
    }

    @PatchMapping("/password/reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
    }

    @PatchMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProfile(@Valid @RequestBody ChangeProfileRequest request) {
        changeProfileUseCase.execute(request);
    }

    @GetMapping("/profile")
    public MyProfileResponse getMyProfile() {
        return getMyProfileUseCase.execute();
    }

    @GetMapping("/account")
    public MyAccountInfoResponse getMyAccountInfo() {
        return getMyAccountInfoUseCase.execute();
    }
}
