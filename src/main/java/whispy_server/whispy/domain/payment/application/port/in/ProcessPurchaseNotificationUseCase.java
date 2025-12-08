package whispy_server.whispy.domain.payment.application.port.in;

import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;

/**
 * 구매 알림 처리 유스케이스.
 *
 * Google Play Pub/Sub 메시지를 처리하는 인바운드 포트입니다.
 */
public interface ProcessPurchaseNotificationUseCase {
    /**
     * Google Play Pub/Sub 메시지를 처리합니다.
     *
     * @param pubSubMessage 처리할 Pub/Sub 메시지
     */
    void processPubSubMessage(PubSubMessageRequest pubSubMessage);
}
