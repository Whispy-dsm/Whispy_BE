package whispy_server.whispy.global.config.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 비동기 처리 설정.
 *
 * Discord 알림 전송 등 부가 기능을 비동기로 처리하여 메인 비즈니스 로직의 성능에 영향을 주지 않도록 합니다.
 * Virtual Thread를 사용하여 효율적인 비동기 처리를 제공합니다.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
