package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.global.feign.google.dto.PubSubMessage;

public interface ProcessPurchaseNotificationUseCase {
    void processPubSubMessage(PubSubMessage pubSubMessage);
}
