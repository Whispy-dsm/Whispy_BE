package whispy_server.whispy.domain.payment.application.service.domain;

import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.exception.domain.payment.UnknownProductIdException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 구독 팩토리.
 *
 * 구독 도메인 모델을 생성하는 팩토리 클래스입니다.
 */
@Component
public class SubscriptionFactory {

    /**
     * 새로운 구독을 생성합니다.
     *
     * @param email 사용자 이메일
     * @param purchaseToken 구매 토큰
     * @param googlePlayProductId Google Play 상품 ID
     * @param info Google Play 구독 정보
     * @return 새로 생성된 구독
     */
    public Subscription createNewSubscription(String email, String purchaseToken, String googlePlayProductId, GooglePlaySubscriptionInfo info) {
        return new Subscription(
                null,
                email,
                purchaseToken,
                getProductTypeFromGooglePlayId(googlePlayProductId),
                toLocalDateTime(info.startTimeMillis()),
                SubscriptionState.ACTIVE,
                true,
                toLocalDateTime(info.expiryTimeMillis())
        );
    }

    /**
     * 기존 구독을 갱신합니다.
     *
     * @param subscription 기존 구독
     * @param newExpiryTime 새로운 만료 시간
     * @return 갱신된 구독
     */
    public Subscription renewedFrom(Subscription subscription, LocalDateTime newExpiryTime){
        return new Subscription(
                subscription.id(),
                subscription.email(),
                subscription.purchaseToken(),
                subscription.productType(),
                subscription.purchaseTime(),
                SubscriptionState.ACTIVE,
                subscription.autoRenewing(),
                newExpiryTime
        );
    }

    /**
     * 구독의 상태를 변경합니다.
     *
     * @param subscription 기존 구독
     * @param newState 새로운 상태
     * @return 상태가 변경된 구독
     */
    public Subscription withState(Subscription subscription, SubscriptionState newState) {
        return new Subscription(
                subscription.id(),
                subscription.email(),
                subscription.purchaseToken(),
                subscription.productType(),
                subscription.purchaseTime(),
                newState,
                subscription.autoRenewing(),
                subscription.expiryTime()
        );
    }

        /**
         * Google Play 상품 ID로부터 상품 타입을 가져옵니다.
         *
         * @param googlePlayId Google Play 상품 ID
         * @return 상품 타입
         * @throws UnknownProductIdException 알 수 없는 상품 ID인 경우
         */
        private ProductType getProductTypeFromGooglePlayId (String googlePlayId){
            return switch (googlePlayId) {
                case "monthly_basic" -> ProductType.MONTHLY;
                default -> throw UnknownProductIdException.EXCEPTION;
            };
        }

        /**
         * epoch 밀리초를 LocalDateTime으로 변환합니다.
         *
         * @param epochMillis epoch 밀리초
         * @return LocalDateTime
         */
        private LocalDateTime toLocalDateTime(long epochMillis){
        return LocalDateTime.ofEpochSecond(epochMillis/ 1000, 0, ZoneOffset.UTC);
        }

    }

