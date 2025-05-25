package whispy_server.whispy.global.exception.domain.oauth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class UnsupportedProviderException extends WhispyException {

    public static final WhispyException EXCEPTION = new UnsupportedProviderException();

    public UnsupportedProviderException() {
        super(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
    }
}
