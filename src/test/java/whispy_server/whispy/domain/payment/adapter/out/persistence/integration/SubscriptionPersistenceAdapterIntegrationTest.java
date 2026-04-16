package whispy_server.whispy.domain.payment.adapter.out.persistence.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.adapter.out.persistence.SubscriptionPersistenceAdapter;
import whispy_server.whispy.domain.payment.adapter.out.persistence.repository.SubscriptionJpaRepository;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.support.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubscriptionPersistenceAdapter integration tests")
class SubscriptionPersistenceAdapterIntegrationTest extends IntegrationTestSupport {

    private static final String TEST_EMAIL = "subscriber@example.com";

    @Autowired
    private SubscriptionPersistenceAdapter subscriptionPersistenceAdapter;

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Test
    @DisplayName("treats GRACE_PERIOD as a current subscription for entitlement lookups")
    void whenGracePeriodSubscriptionExists_thenReturnsCurrentSubscription() {
        subscriptionJpaRepository.save(createSubscriptionEntity("grace-token-123", SubscriptionState.GRACE_PERIOD, LocalDateTime.now().plusDays(5)));

        Optional<Subscription> subscription = subscriptionPersistenceAdapter.findCurrentSubscriptionByEmail(TEST_EMAIL);

        assertThat(subscription).isPresent();
        assertThat(subscription.get().subscriptionState()).isEqualTo(SubscriptionState.GRACE_PERIOD);
        assertThat(subscription.get().isActive()).isTrue();
    }

    @Test
    @DisplayName("treats non-expired canceled subscriptions as current entitlements")
    void whenCanceledSubscriptionNotExpired_thenReturnsCurrentSubscription() {
        subscriptionJpaRepository.save(createSubscriptionEntity("canceled-token-123", SubscriptionState.CANCELED, LocalDateTime.now().plusDays(3)));

        Optional<Subscription> subscription = subscriptionPersistenceAdapter.findCurrentSubscriptionByEmail(TEST_EMAIL);

        assertThat(subscription).isPresent();
        assertThat(subscription.get().subscriptionState()).isEqualTo(SubscriptionState.CANCELED);
        assertThat(subscription.get().isActive()).isTrue();
    }

    private SubscriptionJpaEntity createSubscriptionEntity(String purchaseToken, SubscriptionState state, LocalDateTime expiryTime) {
        return SubscriptionJpaEntity.builder()
                .email(TEST_EMAIL)
                .purchaseToken(purchaseToken)
                .productType(ProductType.MONTHLY)
                .purchaseTime(LocalDateTime.now().minusDays(10))
                .subscriptionState(state)
                .autoRenewing(Boolean.FALSE)
                .expiryTime(expiryTime)
                .build();
    }
}
