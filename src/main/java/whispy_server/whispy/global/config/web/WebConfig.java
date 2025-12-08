package whispy_server.whispy.global.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import whispy_server.whispy.global.file.FileProperties;

/**
 * 정적 리소스 핸들링과 CORS 정책을 정의하는 WebMvc 설정.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final FileProperties fileProperties;
    private static final String FILE = "file:";
    
    /**
     * 업로드된 파일을 `/file/**` 경로로 노출한다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**")
               .addResourceLocations(FILE + fileProperties.uploadPath() + "/");
    }
    
    /**
     * 전역 CORS 허용 정책을 설정한다.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
