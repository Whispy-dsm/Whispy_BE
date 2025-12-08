package whispy_server.whispy.global.feign.google.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import whispy_server.whispy.global.feign.config.GooglePlayFeignConfig;
import whispy_server.whispy.global.feign.google.dto.response.GooglePlaySubscriptionInfoResponse;

/**
 * Google Play Developer API를 호출하기 위한 Feign 클라이언트.
 */
@FeignClient(name = "google-play-api", url = "https://androidpublisher.googleapis.com/androidpublisher/v3", configuration = GooglePlayFeignConfig.class)
public interface GooglePlayFeignClient {

    /**
     * 구독 상태를 조회한다.
     */
    @GetMapping("/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{purchaseToken}")
    GooglePlaySubscriptionInfoResponse getSubscriptionInfo(
            @PathVariable("packageName") String packageName,
            @PathVariable("subscriptionId") String subscriptionId,
            @PathVariable("purchaseToken") String purchaseToken
    );

    /**
     * 구독 결제를 승인(acknowledge)한다.
     */
    @PostMapping("/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{purchaseToken}:acknowledge")
    void acknowledgeSubscription(
            @PathVariable("packageName") String packageName,
            @PathVariable("subscriptionId") String subscriptionId,
            @PathVariable("purchaseToken") String purchaseToken
    );

}
