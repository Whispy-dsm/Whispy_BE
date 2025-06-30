package whispy_server.whispy.global.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape =  JsonFormat.Shape.OBJECT)
public enum ErrorCode {


    INTERNAL_SERVER_ERROR(500, "서버 오류 발생"),

    INVALID_KAKAO_OAUTH_RESPONSE(400, "OAuth 응답이 유효하지 않습니다."),

    USER_NOT_FOUND(404, "일치하는 유저를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(404, "일치하는 어드민을 찾을 수 없습니다"),

    FEIGN_UNAUTHORIZED(401, "Feign UnAuthorized"),
    INVALID_TOKEN(401, "Invalid Token"),
    INVALID_KAKAO_ACCESS_TOKEN(401, "유효하지 않은 kakao access 토큰입니다."),
    EXPIRED_TOKEN(401, "토큰이 만료 되었습니다."),
    OAUTH_AUTHENTICATION_FAILED(401, "OAuth2 인증에 실패했습니다"),
    PASSWORD_MISS_MATCH(401, "비밀번호가 일치하지 않습니다"),

    UNSUPPORTED_OAUTH_PROVIDER(400, "지원하지 않는 OAuth2 제공자입니다"),

    USER_ALREADY_EXIST(409, "유저가 이미 존재합니다");




    private final int statusCode;
    private final String message;



}
