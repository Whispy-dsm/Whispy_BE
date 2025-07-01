package whispy_server.whispy.global.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape =  JsonFormat.Shape.OBJECT)
public enum ErrorCode {


    INTERNAL_SERVER_ERROR(500, "서버 오류 발생"),
    FILE_DELETE_FAILED(500, "파일 삭제에 실패했습니다"),
    FILE_UPLOAD_FAILED(500, "파일 업로드에 실패했습니다"),

    INVALID_KAKAO_OAUTH_RESPONSE(400, "OAuth 응답이 유효하지 않습니다."),
    UNSUPPORTED_OAUTH_PROVIDER(400, "지원하지 않는 OAuth2 제공자입니다"),
    FILE_NAME_EMPTY(400, "파일명이 비어있습니다"),
    FILE_NAME_CONTAINS_PATH(400, "파일명에 경로 문자가 포함되어 있습니다"),
    FILE_NAME_INVALID_CHAR(400, "파일명에 허용되지 않은 문자가 포함되어 있습니다"),
    FILE_NAME_TOO_LONG(400, "파일명이 너무 깁니다"),
    FILE_NO_EXTENSION(400, "파일 확장자가 없습니다"),
    FILE_INVALID_EXTENSION(400, "지원하지 않는 파일 확장자입니다"),
    FILE_INVALID_MIME_TYPE(400, "지원하지 않는 파일 타입입니다"),
    FILE_SIZE_EXCEEDED(400, "파일 크기가 제한을 초과했습니다"),

    USER_NOT_FOUND(404, "일치하는 유저를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(404, "일치하는 어드민을 찾을 수 없습니다"),
    FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다"),

    FEIGN_UNAUTHORIZED(401, "Feign UnAuthorized"),
    INVALID_TOKEN(401, "Invalid Token"),
    INVALID_KAKAO_ACCESS_TOKEN(401, "유효하지 않은 kakao access 토큰입니다."),
    EXPIRED_TOKEN(401, "토큰이 만료 되었습니다."),
    OAUTH_AUTHENTICATION_FAILED(401, "OAuth2 인증에 실패했습니다"),
    PASSWORD_MISS_MATCH(401, "비밀번호가 일치하지 않습니다"),

    USER_ALREADY_EXIST(409, "유저가 이미 존재합니다");







    private final int statusCode;
    private final String message;



}
