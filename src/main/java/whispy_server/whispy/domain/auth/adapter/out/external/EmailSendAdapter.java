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

/**
 * 이메일 발송 아웃바운드 어댑터
 *
 * 외부 메일 서버를 통해 이메일을 발송하는 아웃바운드 어댑터입니다.
 * Thymeleaf 템플릿을 사용하여 HTML 형식의 인증 이메일을 발송합니다.
 */
@Component
@RequiredArgsConstructor
public class EmailSendAdapter implements EmailSendPort {

    private static final String VERIFICATION_SUBJECT = "Whispy 이메일 인증 코드";
    private static final String VERIFICATION_TEMPLATE = "email/verification-email";
    private static final String EMAIL_FROM = "Whispy <noreply@whispy.app>";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * 이메일로 인증 코드를 발송합니다.
     *
     * Thymeleaf 템플릿을 렌더링하여 HTML 형식의 인증 이메일을 생성하고 발송합니다.
     * 발송 실패 시 EmailSendFailedException을 발생시킵니다.
     *
     * @param email 인증 코드를 받을 이메일 주소
     * @param code 6자리 인증 코드
     * @throws EmailSendFailedException 이메일 발송 실패 시
     */
    @Override
    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = createMessage(email, code);
            mailSender.send(message);

        } catch (Exception e) {
            throw new EmailSendFailedException(e);
        }
    }

    /**
     * 이메일 메시지를 생성합니다.
     *
     * Thymeleaf 템플릿 엔진을 사용하여 HTML 형식의 이메일 본문을 생성합니다.
     * 템플릿 경로: resources/templates/email/verification-email.html
     *
     * @param email 수신자 이메일 주소
     * @param code  인증 코드 (템플릿에 삽입)
     * @return 생성된 MimeMessage
     * @throws MessagingException 메시지 생성 실패 시
     */
    private MimeMessage createMessage(String email, String code) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(EMAIL_FROM);
        helper.setTo(email);
        helper.setSubject(VERIFICATION_SUBJECT);

        Context ctx = new Context();
        ctx.setVariable("code", code);

        String htmlContent = templateEngine.process(VERIFICATION_TEMPLATE, ctx);
        helper.setText(htmlContent, true);

        return mimeMessage;
    }
}

