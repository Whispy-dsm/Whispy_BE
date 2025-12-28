package whispy_server.whispy.global.feign.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.feign.discord.client.DiscordBugClient;
import whispy_server.whispy.global.feign.discord.client.DiscordLogClient;
import whispy_server.whispy.global.feign.discord.dto.DiscordEmbed;
import whispy_server.whispy.global.feign.discord.dto.DiscordPayload;

import java.util.List;

/**
 * Discord Webhook을 이용해 에러 및 로그 정보를 전송하는 서비스.
 *
 * 예외는 예외 전용 웹훅으로, 일반 로그는 로그 전용 웹훅으로 전송한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordNotificationService {

    private final DiscordBugClient discordBugClient;
    private final DiscordLogClient discordLogClient;

    /**
     * 예외 정보를 Embed 형태로 구성해 Discord 예외 웹훅으로 전송한다.
     * 비동기로 처리되어 메인 비즈니스 로직의 성능에 영향을 주지 않습니다.
     *
     * @param exception 전송할 예외 객체
     */
    @Async
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
     * 로그 정보를 Embed 형태로 구성해 Discord 로그 웹훅으로 전송한다.
     * 비동기로 처리되어 메인 비즈니스 로직의 성능에 영향을 주지 않습니다.
     *
     * @param level 로그 레벨 (ERROR, WARN, INFO, DEBUG)
     * @param message 로그 메시지
     * @param errorCode 에러 코드 (WhispyException의 ErrorCode)
     */
    @Async
    public void sendLogNotification(String level, String message, String errorCode) {
        try {
            String logMessage = String.format(
                    "**로그 레벨**: `%s`\n" +
                    "**에러 코드**: `%s`\n" +
                    "**메시지**: %s",
                    level,
                    errorCode,
                    message
            );

            int color = getColorByLevel(level);
            DiscordEmbed embeds = new DiscordEmbed("📝 로그 발생", logMessage, color);
            DiscordPayload payload = new DiscordPayload(List.of(embeds));
            discordLogClient.sendWebhook(payload);

        } catch (Exception e) {
            log.error("Discord 로그 알림 전송 실패", e);
        }
    }

    /**
     * WhispyException 여부에 따라 Discord 메시지 본문을 구성한다.
     *
     * @param exception 메시지를 구성할 예외 객체
     * @return Discord Embed 본문 문자열
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

    /**
     * 로그 레벨에 따라 Discord Embed 색상을 반환한다.
     *
     * @param level 로그 레벨 (ERROR, WARN, INFO, DEBUG 등)
     * @return Discord Embed 색상 코드 (10진수)
     */
    private int getColorByLevel(String level) {
        return switch (level) {
            case "ERROR" -> 15158332;  // 빨강
            case "WARN" -> 16776960;   // 노랑
            case "INFO" -> 3447003;    // 파랑
            default -> 9807270;        // 회색
        };
    }
}
