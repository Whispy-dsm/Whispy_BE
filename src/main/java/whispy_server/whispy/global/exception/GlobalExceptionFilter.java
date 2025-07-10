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

@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final DiscordNotificationService discordNotificationService;
    private final ErrorNotificationHandler errorNotificationHandler;

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


    private void writeErrorResponse(HttpServletResponse response, int statusCode, ErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

