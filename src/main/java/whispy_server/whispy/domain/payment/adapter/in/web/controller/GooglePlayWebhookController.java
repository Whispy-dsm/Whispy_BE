package whispy_server.whispy.domain.payment.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;
import whispy_server.whispy.domain.payment.application.port.in.ProcessPurchaseNotificationUseCase;
import whispy_server.whispy.global.document.api.payment.GooglePlayWebhookApiDocument;

/**
 * Google Play 웹훅 REST 컨트롤러.
 *
 * Google Play Pub/Sub 알림을 수신하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class GooglePlayWebhookController implements GooglePlayWebhookApiDocument {

    private final ProcessPurchaseNotificationUseCase processPurchaseNotificationUseCase;

    /**
     * Google Play 알림을 처리합니다.
     *
     * @param pubSubMessage Google Play Pub/Sub 메시지
     */
    @PostMapping("/google-play")
    public void handleGooglePlayNotification(@RequestBody PubSubMessageRequest pubSubMessage) {
        processPurchaseNotificationUseCase.processPubSubMessage(pubSubMessage);
    }
}
