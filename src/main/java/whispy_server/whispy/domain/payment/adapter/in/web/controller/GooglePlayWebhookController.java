package whispy_server.whispy.domain.payment.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;
import whispy_server.whispy.domain.payment.application.port.in.ProcessPurchaseNotificationUseCase;
import whispy_server.whispy.global.document.api.payment.GooglePlayWebhookApiDocument;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class GooglePlayWebhookController implements GooglePlayWebhookApiDocument {

    private final ProcessPurchaseNotificationUseCase processPurchaseNotificationUseCase;

    @PostMapping("/google-play")
    public void handleGooglePlayNotification(@RequestBody PubSubMessageRequest pubSubMessage) {
        processPurchaseNotificationUseCase.processPubSubMessage(pubSubMessage);
    }
}
