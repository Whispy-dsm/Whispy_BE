package whispy_server.whispy.global.webhook.discord.dto;

import java.util.List;

public record DiscordPayload(List<DiscordEmbed> embeds) {
}
