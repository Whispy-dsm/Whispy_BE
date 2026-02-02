package whispy_server.whispy.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.exception.error.ErrorResponse;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

import java.io.IOException;

/**
 * Filter 단계에서 발생한 예외를 잡아 표준 ErrorResponse 로 응답하고 모니터링에 전파하는 필터.
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final DiscordNotificationService discordNotificationService;
    private final ErrorNotificationHandler errorNotificationHandler;

    /**
     * 체인 수행 중 발생한 예외를 처리한다.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try{
            filterChain.doFilter(request,response);
        }catch (WhispyException e){
            log.debug("[Filter] Whispy 예외 포착 - URI: {}, ErrorCode: {}",
                request.getRequestURI(), e.getErrorCode());
            errorNotificationHandler.handleWhispyException(e);

            ErrorCode errorCode = e.getErrorCode();
            writeErrorResponse(response, errorCode.getStatusCode(), ErrorResponse.of(errorCode));
        } catch (Exception e){
            log.debug("[Filter] 일반 예외 포착 - URI: {}, ExceptionType: {}",
                request.getRequestURI(), e.getClass().getSimpleName());
            errorNotificationHandler.handleExceptionException(e);

            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ErrorResponse.of(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"));
        }

    }


    /**
     * JSON 형태의 오류 응답을 작성합니다.
     *
     * HTTP 응답의 상태 코드를 설정하고, ErrorResponse 객체를 JSON으로 직렬화하여
     * 응답 본문에 작성합니다.
     *
     * @param response      HttpServletResponse 객체
     * @param statusCode    HTTP 상태 코드
     * @param errorResponse 오류 응답 DTO
     * @throws IOException JSON 쓰기 실패 시
     */
    private void writeErrorResponse(HttpServletResponse response, int statusCode, ErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

