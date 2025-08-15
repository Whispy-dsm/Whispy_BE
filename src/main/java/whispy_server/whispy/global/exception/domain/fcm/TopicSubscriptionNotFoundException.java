package whispy_server.whispy.global.exception.domain.fcm;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class TopicSubscriptionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new TopicSubscriptionNotFoundException();

    private TopicSubscriptionNotFoundException() {
        super(ErrorCode.TOPIC_SUBSCRIPTION_NOT_FOUND);
    }
}
