package whispy_server.whispy.global.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 도메인 전역에서 사용하는 표준 오류 코드 정의.
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape =  JsonFormat.Shape.OBJECT)
public enum ErrorCode {

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
    IMAGE_INVALID(400, "유효하지 않은 이미지 파일입니다"),
    INVALID_PAYMENT_STATE(400, "결제 상태가 유효하지 않습니다"),
    UNKNOWN_PRODUCT_ID(400, "알 수 없는 상품 ID입니다"),
    INVALID_PURCHASE_TOKEN(400, "유효하지 않은 구매 토큰입니다"),
    INVALID_SUBSCRIPTION_NOTIFICATION(400, "유효하지 않은 구독 알림입니다"),
    INVALID_FOCUS_SESSION_TIME_RANGE(400, "종료시간이 시작시간보다 이를 수 없습니다."),
    FOCUS_SESSION_DURATION_EXCEEDED(400, "집중시간이 전체 시간을 초과할 수 없습니다."),
    INVALID_FOCUS_SESSION_DURATION(400, "집중시간은 1초 이상이어야 합니다."),
    INVALID_SLEEP_SESSION_TIME_RANGE(400, "종료시간이 시작시간보다 이를 수 없습니다."),
    SLEEP_SESSION_DURATION_EXCEEDED(400, "수면시간이 전체 시간을 초과할 수 없습니다."),
    INVALID_SLEEP_SESSION_DURATION(400, "수면시간은 1초 이상이어야 합니다."),
    INVALID_MEDITATION_SESSION_TIME_RANGE(400, "종료시간이 시작시간보다 이를 수 없습니다."),
    MEDITATION_SESSION_DURATION_EXCEEDED(400, "명상시간이 전체 시간을 초과할 수 없습니다."),
    INVALID_MEDITATION_SESSION_DURATION(400, "명상시간은 1초 이상이어야 합니다."),
    INVALID_STATISTICS_DATE(400, "통계 조회는 미래 날짜로 할 수 없습니다."),
    INVALID_DATE_RANGE(400, "시작 날짜가 종료 날짜보다 늦을 수 없습니다."),
    DATE_RANGE_EXCEEDED(400, "조회 기간은 최대 1년까지 가능합니다."),
    INVALID_WITHDRAWAL_REASON_DETAIL(400, "탈퇴 사유 상세 내용이 유효하지 않습니다."),

    USER_NOT_FOUND(404, "일치하는 유저를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(404, "일치하는 어드민을 찾을 수 없습니다"),
    FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다"),
    SUBSCRIPTION_NOT_FOUND(404, "구독을 찾을 수 없습니다"),
    PURCHASE_NOT_FOUND(404, "구매 정보를 찾을 수 없습니다"),
    TOPIC_SUBSCRIPTION_NOT_FOUND(404, "토픽 구독 정보를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(404, "알림을 찾을 수 없습니다."),
    MUSIC_NOT_FOUND(404, "음악을 찾을 수 없습니다."),
    ANNOUNCEMENT_NOT_FOUND(404, "공지사항을 찾을 수 없습니다."),
    FOCUS_SESSION_NOT_FOUND(404, "집중 세션을 찾을 수 없습니다."),
    SLEEP_SESSION_NOT_FOUND(404, "수면 세션을 찾을 수 없습니다."),
    MEDITATION_SESSION_NOT_FOUND(404, "명상 세션을 찾을 수 없습니다."),
    WITHDRAWAL_REASON_NOT_FOUND(404, "탈퇴 사유를 찾을 수 없습니다."),

    INVALID_TOKEN(401, "Invalid Token"),
    INVALID_KAKAO_ACCESS_TOKEN(401, "유효하지 않은 kakao access 토큰입니다."),
    EXPIRED_TOKEN(401, "토큰이 만료 되었습니다."),
    TOKEN_TOO_LARGE(401, "토큰 크기가 제한을 초과했습니다."),
    OAUTH_AUTHENTICATION_FAILED(401, "OAuth2 인증에 실패했습니다"),
    PASSWORD_MISS_MATCH(401, "비밀번호가 일치하지 않습니다"),
    EMAIL_NOT_VERIFIED(401, "이메일 인증이 되지 않았습니다."),

    USER_ALREADY_EXIST(409, "유저가 이미 존재합니다"),
    PURCHASE_ALREADY_PROCESSED(409, "이미 처리된 구매입니다"),
    EMAIL_ALREADY_SENT(409, "이미 발송된 인증 코드가 있습니다. 발송된 인증 코드를 입력하거나 5분 후 다시 요청해주세요."),

    EMAIL_RATE_LIMIT_EXCEEDED(429, "이메일 발송 요청이 너무 빈번합니다. 1분 후 다시 시도해주세요."),

    INTERNAL_SERVER_ERROR(500, "서버 오류 발생"),
    FILE_DELETE_FAILED(500, "파일 삭제에 실패했습니다"),
    FILE_UPLOAD_FAILED(500, "파일 업로드에 실패했습니다"),
    WEBP_CONVERTER_NOT_FOUND(500, "WebP 변환기를 찾을 수 없습니다"),
    GOOGLE_PLAY_API_ERROR(500, "Google Play API 호출 중 오류가 발생했습니다"),
    PURCHASE_NOTIFICATION_PROCESSING_FAILED(500, "구매 알림 처리 중 오류가 발생했습니다"),
    SUBSCRIPTION_ACKNOWLEDGMENT_FAILED(500, "구독 승인 처리 중 오류가 발생했습니다"),
    EMAIL_SEND_FAILED(500, "이메일 발송에 실패했습니다. 다시 시도해주세요."),
    FCM_SEND_FAILED(500, "FCM 메시지 전송에 실패했습니다."),
    BATCH_JOB_EXECUTION_FAILED(500, "배치 작업 실행에 실패했습니다."),
    BATCH_ITEM_READER_INITIALIZATION_FAILED(500, "배치 ItemReader 초기화에 실패했습니다."),
    ANNOUNCEMENT_PUBLICATION_FAILED(500, "공지사항 발행에 실패했습니다.");


    private final int statusCode;
    private final String message;
}
