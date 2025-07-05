package whispy_server.whispy.global.webhook.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import whispy_server.whispy.global.webhook.discord.dto.DiscordEmbed;
import whispy_server.whispy.global.webhook.discord.dto.DiscordPayload;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordNotificationService {

    @Value("${spring.discord.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public void sendErrorNotification(Exception exception) {
        String errorMessage = getErrorMessage(exception);

        try {
            DiscordEmbed embeds = new DiscordEmbed("🚨 서버 에러 발생", errorMessage, 15158332);
            DiscordPayload payload = new DiscordPayload(List.of(embeds));
            String json = objectMapper.writeValueAsString(payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json, headers);
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
