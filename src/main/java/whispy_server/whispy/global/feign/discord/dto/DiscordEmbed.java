package whispy_server.whispy.global.feign.discord.dto;

/**
 * Discord Webhook 메시지의 Embed 요소를 표현하는 레코드.
 */
public record DiscordEmbed(
        String title,
        String description,
        int color
) {

}
