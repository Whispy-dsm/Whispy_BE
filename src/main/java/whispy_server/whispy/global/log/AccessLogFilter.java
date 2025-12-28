package whispy_server.whispy.global.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import whispy_server.whispy.global.utils.security.SecurityUtil;

import java.io.IOException;
import java.util.List;

/**
 * 모든 HTTP 요청/응답을 로깅하는 필터.
 * 헬스 체크, 정적 리소스 등 불필요한 엔드포인트는 로깅에서 제외합니다.
 */
@Slf4j
@Component
public class AccessLogFilter extends OncePerRequestFilter {

    /**
     * 로깅에서 제외할 URI 패턴 목록.
     * 헬스 체크, 정적 리소스, API 문서 등 로그 노이즈를 유발하는 엔드포인트를 제외합니다.
     */
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/actuator/",       // Spring Actuator 엔드포인트 (헬스 체크, 메트릭 등)
            "/favicon.ico",     // 브라우저 파비콘 요청
            "/file/",           // 정적 파일 제공 (이미지, 음악, 비디오)
            "/swagger-ui/",     // Swagger UI
            "/v3/api-docs"      // OpenAPI 문서
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return EXCLUDED_PATHS.stream()
                .anyMatch(uri::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String userIdentifier = SecurityUtil.getCurrentUserIdentifier();

            log.info("[ACCESS] {} {} → {} ({}ms) - User: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    userIdentifier
            );
        }
    }
}
