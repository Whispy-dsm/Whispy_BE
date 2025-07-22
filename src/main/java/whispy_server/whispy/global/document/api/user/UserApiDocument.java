package whispy_server.whispy.global.document.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.KakaoOauthTokenRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;

@Tag(name = "USER API", description = "사용자 관련 API")
public interface UserApiDocument {

    @Operation(summary = "사용자 로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않습니다"),
            @ApiResponse(responseCode = "404", description = "일치하는 유저를 찾을 수 없습니다"),
            @ApiResponse(responseCode = "423", description = "계정이 잠겨있습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    TokenResponse login(UserLoginRequest request);

    @Operation(summary = "사용자 회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "409", description = "유저가 이미 존재합니다"),
            @ApiResponse(responseCode = "422", description = "비밀번호 정책에 맞지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void register(RegisterRequest request);

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 리프레시 토큰"),
            @ApiResponse(responseCode = "404", description = "토큰에 해당하는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    TokenResponse reissue(@Parameter(description = "리프레시 토큰", required = true, in = ParameterIn.HEADER, name = "X-Refresh-Token") String token);

    @Operation(summary = "카카오 OAuth 로그인", description = "카카오 액세스 토큰으로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카카오 로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 카카오 토큰"),
            @ApiResponse(responseCode = "403", description = "카카오 API 접근 권한 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생"),
            @ApiResponse(responseCode = "502", description = "카카오 API 연결 오류"),
            @ApiResponse(responseCode = "503", description = "카카오 서비스 일시적 오류")
    })
    TokenResponse authenticateWithKakaoToken(KakaoOauthTokenRequest request);

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void logout();

    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자 계정을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "탈퇴 권한 없음"),
            @ApiResponse(responseCode = "409", description = "진행 중인 구독이 있어 탈퇴 불가"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void withdrawal();
}