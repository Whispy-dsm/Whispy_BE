package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;

public interface ProcessPurchaseNotificationUseCase {
    void processPubSubMessage(PubSubMessageRequest pubSubMessage);
}
