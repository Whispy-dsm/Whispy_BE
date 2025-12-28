package whispy_server.whispy;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * Whispy 백엔드 애플리케이션의 진입점으로, Spring Boot 컨텍스트를 구동한다.
 */
@SpringBootApplication
public class WhispyBeApplication {

    /**
     * 애플리케이션 시작 시 JVM 기본 타임존을 한국 시간(Asia/Seoul)으로 설정한다.
     * 이를 통해 LocalDate.now(), LocalDateTime.now() 등이 한국 시간 기준으로 동작한다.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(WhispyBeApplication.class, args);
    }

}

