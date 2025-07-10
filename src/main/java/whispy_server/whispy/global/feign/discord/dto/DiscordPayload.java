package whispy_server.whispy.global.feign.discord.dto;

import java.util.List;

public record DiscordPayload(List<DiscordEmbed> embeds) {
}
