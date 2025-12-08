package whispy_server.whispy.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.exception.error.ErrorResponse;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

import java.io.IOException;

/**
 * Filter 단계에서 발생한 예외를 잡아 표준 ErrorResponse 로 응답하고 모니터링에 전파하는 필터.
 */
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
            errorNotificationHandler.handleWhispyException(e);

            ErrorCode errorCode = e.getErrorCode();
            writeErrorResponse(response, errorCode.getStatusCode(), ErrorResponse.of(errorCode, errorCode.getMessage(), e));
        } catch (Exception e){
            errorNotificationHandler.handleExceptionException(e);
            e.printStackTrace();

            writeErrorResponse(response, response.getStatus(), ErrorResponse.of(response.getStatus(),e.getMessage(), e));
        }

    }


    /**
     * JSON 형태의 오류 응답을 작성한다.
     */
    private void writeErrorResponse(HttpServletResponse response, int statusCode, ErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

