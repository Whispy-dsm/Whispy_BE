package whispy_server.whispy.global.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 애플리케이션 전역에서 사용하는 RestTemplate 빈을 등록하는 설정.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 간단한 RestTemplate 인스턴스를 빈으로 제공한다.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
