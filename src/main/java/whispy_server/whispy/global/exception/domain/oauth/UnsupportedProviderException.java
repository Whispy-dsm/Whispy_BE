package whispy_server.whispy.global.exception.domain.oauth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.UNSUPPORTED_OAUTH_PROVIDER 상황을 나타내는 도메인 예외.
 */
public class UnsupportedProviderException extends WhispyException {

    public static final WhispyException EXCEPTION = new UnsupportedProviderException();

    public UnsupportedProviderException() {
        super(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
    }
}
