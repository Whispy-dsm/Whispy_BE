package whispy_server.whispy.domain.auth.application.port.out;

/**
 * 이메일 발송 아웃바운드 포트
 *
 * 외부 메일 서버를 통해 이메일을 발송하는 아웃바운드 포트입니다.
 * 이메일 인증 코드 발송 기능을 제공합니다.
 */
public interface EmailSendPort {
    /**
     * 이메일로 인증 코드를 발송합니다.
     *
     * @param email 인증 코드를 받을 이메일 주소
     * @param code 6자리 인증 코드
     */
    void sendVerificationCode(String email, String code);
}
