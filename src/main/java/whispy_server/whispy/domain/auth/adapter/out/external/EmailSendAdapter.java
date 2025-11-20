package whispy_server.whispy.domain.auth.adapter.out.external;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import whispy_server.whispy.domain.auth.application.port.out.EmailSendPort;
import whispy_server.whispy.global.exception.domain.auth.EmailSendFailedException;

@Component
@RequiredArgsConstructor
public class EmailSendAdapter implements EmailSendPort {

    private static final String VERIFICATION_SUBJECT = "Whispy 이메일 인증 코드";
    private static final String VERIFICATION_TEMPLATE = "email/verification-email";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = createMessage(email, code);
            mailSender.send(message);

        } catch (Exception e) {
            throw EmailSendFailedException.EXCEPTION;
        }
    }

    private MimeMessage createMessage(String email, String code) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(VERIFICATION_SUBJECT);

        Context ctx = new Context();
        ctx.setVariable("code", code);

        String htmlContent = templateEngine.process(VERIFICATION_TEMPLATE, ctx);
        helper.setText(htmlContent, true);

        return mimeMessage;
    }
}

