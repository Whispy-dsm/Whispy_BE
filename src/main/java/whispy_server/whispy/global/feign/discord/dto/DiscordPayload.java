package whispy_server.whispy.global.feign.discord.dto;

import java.util.List;

/**
 * Discord Webhook 요청 바디.
 */
public record DiscordPayload(List<DiscordEmbed> embeds) {
}
