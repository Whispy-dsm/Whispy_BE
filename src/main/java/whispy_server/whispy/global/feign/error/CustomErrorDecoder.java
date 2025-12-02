package whispy_server.whispy.global.feign.error;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

/**
 * 5xx 응답 시 재시도를 유도하는 커스텀 Feign ErrorDecoder.
 */
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    /**
     * 5xx 오류는 RetryableException으로 감싸 반환한다.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        var exception = defaultErrorDecoder.decode(methodKey, response);

        if (response.status() >= 500) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Long) null,
                    response.request()
            );
        }

        return exception;
    }
}

