package whispy_server.whispy.global.config.timezone;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * 애플리케이션 타임존 설정.
 */
@Configuration
public class TimeZoneConfig {

    /**
     * 애플리케이션 시작 시 JVM 기본 타임존을 한국 시간(Asia/Seoul)으로 설정한다.
     * 이를 통해 LocalDate.now(), LocalDateTime.now() 등이 한국 시간 기준으로 동작한다.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
