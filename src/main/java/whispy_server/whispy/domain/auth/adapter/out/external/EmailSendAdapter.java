package whispy_server.whispy.domain.auth.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.auth.application.port.out.EmailSendPort;
import whispy_server.whispy.global.exception.domain.auth.EmailSendFailedException;

@Component
@RequiredArgsConstructor
public class EmailSendAdapter implements EmailSendPort {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String email, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Whispy 이메일 인증 코드");
            message.setText(String.format(
                            "안녕하세요!\n\n" +
                            "Whispy 이메일 인증 코드입니다.\n\n" +
                            "인증 코드: %s\n\n" +
                            "이 코드는 5분 후 만료됩니다.\n" +
                            "본인이 요청하지 않았다면 이 메일을 무시해주세요.",
                            code
            ));
            mailSender.send(message);

        } catch (Exception e) {
            throw EmailSendFailedException.EXCEPTION;
        }
    }
}
