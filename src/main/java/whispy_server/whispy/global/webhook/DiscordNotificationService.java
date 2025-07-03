package whispy_server.whispy.global.webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordNotificationService {

    @Value("${spring.discord.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;

    public void sendErrorNotification(Exception exception) {
        String errorMessage = getErrorMessage(exception)
                .replace("\"","\\\"")
                .replace("\n", "\\n");

        String payload = String.format(
                "{\"embeds\":[{\"title\":\"🚨 서버 에러 발생\",\"description\":\"%s\",\"color\":15158332}]}",
                errorMessage
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(webhookUrl, request, String.class);
        } catch (Exception e) {
            log.error("Discord 알림 전송 실패", e);
        }
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof WhispyException) {
            WhispyException whispyException = (WhispyException) exception;
            ErrorCode errorCode = whispyException.getErrorCode();
            return String.format(
                    "**에러 코드**: `%s`\n" +
                            "**상태 코드**: `%d`\n" +
                            "**메시지**: %s",
                    errorCode.name(),
                    errorCode.getStatusCode(),
                    errorCode.getMessage()
            );
        }
        return String.format(
                        "**예외 타입**: `%s`\n" +
                        "**메시지**: %s",
                exception.getClass().getSimpleName(),
                exception.getMessage() != null ? exception.getMessage() : "메시지 없음"
        );
    }

}
