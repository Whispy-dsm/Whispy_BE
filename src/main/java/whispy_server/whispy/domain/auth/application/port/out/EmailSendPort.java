package whispy_server.whispy.domain.auth.application.port.out;

public interface EmailSendPort {
    void sendVerificationCode(String email, String code);
}
