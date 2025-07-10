package whispy_server.whispy.global.feign.google.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import whispy_server.whispy.global.feign.config.GooglePlayFeignConfig;
import whispy_server.whispy.global.feign.google.dto.response.GooglePlaySubscriptionInfoResponse;

@FeignClient(name = "google-play-api", url = "https://androidpublisher.googleapis.com/androidpublisher/v3", configuration = GooglePlayFeignConfig.class)
public interface GooglePlayFeignClient {

    @GetMapping("/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{purchaseToken}")
    GooglePlaySubscriptionInfoResponse getSubscriptionInfo(
            @PathVariable("packageName") String packageName,
            @PathVariable("subscriptionId") String subscriptionId,
            @PathVariable("purchaseToken") String purchaseToken
    );

    @PostMapping("/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{purchaseToken}:acknowledge")
    void acknowledgeSubscription(
            @PathVariable("packageName") String packageName,
            @PathVariable("subscriptionId") String subscriptionId,
            @PathVariable("purchaseToken") String purchaseToken
    );

}
