package whispy_server.whispy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Whispy 백엔드 애플리케이션의 진입점으로, Spring Boot 컨텍스트를 구동한다.
 */
@SpringBootApplication
public class WhispyBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhispyBeApplication.class, args);
    }

}

