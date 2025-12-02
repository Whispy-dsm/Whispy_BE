package whispy_server.whispy.global.feign.discord.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import whispy_server.whispy.global.feign.discord.dto.DiscordPayload;

/**
 * Discord Webhook 호출을 위한 Feign 클라이언트.
 */
@FeignClient(name = "discord-webhook", url = "${spring.discord.webhook.url}")
public interface DiscordBugClient {

    /**
     * Webhook URL로 Embed payload를 전송한다.
     */
    @PostMapping
    void sendWebhook(@RequestBody DiscordPayload payload);
}
