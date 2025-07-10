package whispy_server.whispy.global.feign.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import whispy_server.whispy.global.feign.error.Custom5xxRetryer;
import whispy_server.whispy.global.feign.error.CustomErrorDecoder;

@EnableFeignClients(basePackages = {"whispy_server.whispy"})
@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Custom5xxRetryer();
    }
}
