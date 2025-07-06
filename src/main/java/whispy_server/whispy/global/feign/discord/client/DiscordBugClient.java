package whispy_server.whispy.global.feign.discord.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import whispy_server.whispy.global.feign.discord.dto.DiscordPayload;

@FeignClient(name = "discord-webhook", url = "${spring.discord.webhook.url}")
public interface DiscordBugClient {

    @PostMapping
    void sendWebhook(@RequestBody DiscordPayload payload);
}
