package whispy_server.whispy.global.config.swagger;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * OpenAPI(Swagger) 관련 설정을 정의한 클래스.
 */
@Configuration
public class SwaggerConfig {

    public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    /**
     * JWT 인증 스키마가 적용된 OpenAPI 메타정보를 구성한다.
     */
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(new Info()
                        .title("Whispy API")
                        .version("v1.0.0")
                        .description("Whispy 서비스 API 문서"))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme));
    }

    /**
     * Swagger ModelResolver가 snake_case 네이밍 전략을 사용하도록 설정한다.
     */
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
    }
}
