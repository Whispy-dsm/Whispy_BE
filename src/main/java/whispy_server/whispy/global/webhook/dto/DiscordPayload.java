package whispy_server.whispy.global.webhook.dto;

import java.util.List;

public record DiscordPayload(List<DiscordEmbed> embeds) {
}
