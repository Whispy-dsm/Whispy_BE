package whispy_server.whispy.global.feign.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.feign.discord.client.DiscordBugClient;
import whispy_server.whispy.global.feign.discord.dto.DiscordEmbed;
import whispy_server.whispy.global.feign.discord.dto.DiscordPayload;

import java.util.List;

/**
 * Discord Webhook을 이용해 에러 정보를 전송하는 서비스.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordNotificationService {

    private final DiscordBugClient discordBugClient;

    /**
     * 예외 정보를 Embed 형태로 구성해 Discord로 전송한다.
     */
    public void sendErrorNotification(Exception exception) {
        String errorMessage = getErrorMessage(exception);

        try {
            DiscordEmbed embeds = new DiscordEmbed("🚨 서버 에러 발생", errorMessage, 15158332);
            DiscordPayload payload = new DiscordPayload(List.of(embeds));
            discordBugClient.sendWebhook(payload);

        } catch (Exception e) {
            log.error("Discord 알림 전송 실패", e);
        }
    }

    /**
     * WhispyException 여부에 따라 Discord 메시지 본문을 구성한다.
     */
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
