package whispy_server.whispy.global.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.whispy.file")
public record FileProperties(
        String uploadPath,
        String baseUrl,
        String profileImageFolder

) {
}
