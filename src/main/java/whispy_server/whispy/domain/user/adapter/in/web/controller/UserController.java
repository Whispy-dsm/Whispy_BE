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
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.OauthCodeExchangeRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ResetPasswordRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.TokenReissueRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyAccountInfoResponse;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.ChangePasswordUseCase;
import whispy_server.whispy.domain.user.application.port.in.ChangeProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.ExchangeOauthCodeUseCase;
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

/**
 * 사용자 관련 REST API를 제공하는 컨트롤러.
 * 헥사고날 아키텍처의 인바운드 어댑터로서 사용자 인증, 프로필 관리, 계정 관리 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApiDocument {

    private final UserLoginUseCase userLoginUseCase;
    private final UserRegisterUseCase userRegisterUseCase;
    private final UserTokenReissueUseCase userTokenReissueUseCase;
    private final KakaoOauthUseCase kakaoOauthUseCase;
    private final ExchangeOauthCodeUseCase exchangeOauthCodeUseCase;
    private final UserLogoutUseCase userLogoutUseCase;
    private final UserWithdrawalUseCase userWithdrawalUseCase;
    private final UpdateFcmTokenUseCase updateFcmTokenUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final GetMyProfileUseCase getMyProfileUseCase;
    private final GetMyAccountInfoUseCase getMyAccountInfoUseCase;
    private final ChangeProfileUseCase changeProfileUseCase;

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param request 이메일, 비밀번호, FCM 토큰을 포함한 로그인 요청
     * @return 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userLoginUseCase.login(request);
    }

    /**
     * 새로운 사용자 회원가입을 처리합니다.
     *
     * @param request 이메일, 비밀번호, 이름, 프로필 정보를 포함한 회원가입 요청
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        userRegisterUseCase.register(request);
    }

    /**
     * 카카오 OAuth 액세스 토큰으로 인증을 처리합니다.
     *
     * @param request 카카오 액세스 토큰과 FCM 토큰
     * @return 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/oauth/kakao")
    public TokenResponse authenticateWithKakaoToken(@Valid @RequestBody KakaoOauthTokenRequest request){
        return kakaoOauthUseCase.loginWithKakao(request);
    }

    /**
     * 앱 딥링크로 전달받은 OAuth 일회용 코드를 JWT 토큰으로 교환합니다.
     *
     * @param request OAuth 코드 교환 요청
     * @return 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/oauth/exchange")
    public TokenResponse exchangeOauthCode(@Valid @RequestBody OauthCodeExchangeRequest request) {
        return exchangeOauthCodeUseCase.execute(request);
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
     *
     * @param token 리프레시 토큰
     * @return 새로운 액세스 토큰과 리프레시 토큰
     */
    @PutMapping("/reissue")
    public TokenResponse reissue(@RequestHeader("X-Refresh-Token") String token) {
        return userTokenReissueUseCase.reissue(new TokenReissueRequest(token));
    }

    /**
     * 현재 인증된 사용자의 로그아웃을 처리합니다.
     */
    @PostMapping("/logout")
    public void logout(){
        userLogoutUseCase.logout();
    }

    /**
     * 현재 인증된 사용자의 회원 탈퇴를 처리합니다.
     */
    @DeleteMapping("/withdrawal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawal(){
        userWithdrawalUseCase.withdrawal();
    }

    /**
     * FCM 토큰을 업데이트합니다.
     *
     * @param request 새로운 FCM 토큰
     */
    @PatchMapping("/fcm-token")
    public void updateFcmToken(@Valid @RequestBody UpdateFcmTokenRequest request) {
        updateFcmTokenUseCase.execute(request);
    }

    /**
     * 인증된 사용자의 비밀번호를 변경합니다.
     *
     * @param request 이메일과 새 비밀번호
     */
    @PostMapping("/password/change")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
    }

    /**
     * 이메일 인증을 통한 비밀번호 재설정을 처리합니다.
     *
     * @param request 이메일과 새 비밀번호
     */
    @PatchMapping("/password/reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
    }

    /**
     * 사용자 프로필 정보를 변경합니다.
     *
     * @param request 이름, 프로필 이미지 URL, 성별
     */
    @PatchMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProfile(@Valid @RequestBody ChangeProfileRequest request) {
        changeProfileUseCase.execute(request);
    }

    /**
     * 현재 인증된 사용자의 프로필 정보를 조회합니다.
     *
     * @return 사용자 이름, 프로필 이미지, 가입 후 경과 일수
     */
    @GetMapping("/profile")
    public MyProfileResponse getMyProfile() {
        return getMyProfileUseCase.execute();
    }

    /**
     * 현재 인증된 사용자의 계정 정보를 조회합니다.
     *
     * @return 이메일, 이름, 프로필 이미지, 성별, OAuth 제공자, 마스킹된 비밀번호
     */
    @GetMapping("/account")
    public MyAccountInfoResponse getMyAccountInfo() {
        return getMyAccountInfoUseCase.execute();
    }
}
