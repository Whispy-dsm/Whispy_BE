package whispy_server.whispy.domain.payment.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.SubscriptionSummaryResponse;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.service.GetUserSubscriptionsService;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserSubscriptionsService unit tests")
class GetUserSubscriptionsServiceTest {

    @InjectMocks
    private GetUserSubscriptionsService getUserSubscriptionsService;

    @Mock
    private QuerySubscriptionPort querySubscriptionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String AUTHENTICATED_EMAIL = "authenticated@example.com";
    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";

    @Test
    @DisplayName("returns the subscription when one exists")
    void whenSubscriptionExists_thenReturnsSubscription() {
        Subscription subscription = createSubscription();
        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(subscription));

        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions();

        assertThat(response.subscriptions()).isPresent();
        SubscriptionSummaryResponse summary = response.subscriptions().orElseThrow();
        assertThat(summary.email()).isEqualTo(TEST_EMAIL);
        assertThat(summary.productType()).isEqualTo(ProductType.MONTHLY);
        assertThat(summary.subscriptionState()).isEqualTo(SubscriptionState.ACTIVE);
    }

    @Test
    @DisplayName("returns an empty optional when the subscription does not exist")
    void whenNoSubscriptionExists_thenReturnsEmptyOptional() {
        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.empty());

        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions();

        assertThat(response.subscriptions()).isEmpty();
    }

    @Test
    @DisplayName("uses the authenticated user email instead of the request email")
    void whenGettingSubscriptions_thenUsesAuthenticatedUserEmail() {
        Subscription subscription = createSubscriptionForEmail(AUTHENTICATED_EMAIL, SubscriptionState.ACTIVE);
        given(userFacadeUseCase.currentUser()).willReturn(createUser(AUTHENTICATED_EMAIL));
        given(querySubscriptionPort.findByEmail(AUTHENTICATED_EMAIL))
                .willReturn(Optional.of(subscription));

        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions(TEST_EMAIL);

        assertThat(response.subscriptions()).isPresent();
        assertThat(response.subscriptions().orElseThrow().email()).isEqualTo(AUTHENTICATED_EMAIL);
        verify(userFacadeUseCase).currentUser();
        verify(querySubscriptionPort).findByEmail(AUTHENTICATED_EMAIL);
        verify(querySubscriptionPort, never()).findByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("returns canceled subscriptions correctly")
    void whenDifferentSubscriptionStates_thenReturnsCorrectly() {
        Subscription canceledSubscription = createSubscriptionWithState(SubscriptionState.CANCELED);
        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(canceledSubscription));

        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions();

        assertThat(response.subscriptions()).isPresent();
        assertThat(response.subscriptions().orElseThrow().subscriptionState()).isEqualTo(SubscriptionState.CANCELED);
    }

    @Test
    @DisplayName("returns expired subscriptions correctly")
    void whenDifferentSubscriptionStatesAgain_thenReturnsCorrectly() {
        Subscription expiredSubscription = createSubscriptionWithState(SubscriptionState.EXPIRED);
        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expiredSubscription));

        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions();

        assertThat(response.subscriptions()).isPresent();
        assertThat(response.subscriptions().orElseThrow().subscriptionState()).isEqualTo(SubscriptionState.EXPIRED);
    }

    private Subscription createSubscription() {
        return createSubscriptionForEmail(TEST_EMAIL, SubscriptionState.ACTIVE);
    }

    private Subscription createSubscriptionWithState(SubscriptionState state) {
        return createSubscriptionForEmail(TEST_EMAIL, state);
    }

    private Subscription createSubscriptionForEmail(String email, SubscriptionState state) {
        return new Subscription(
                1L,
                email,
                TEST_PURCHASE_TOKEN,
                ProductType.MONTHLY,
                LocalDateTime.now().minusDays(15),
                state,
                true,
                LocalDateTime.now().plusDays(15)
        );
    }

    private User createUser() {
        return createUser(TEST_EMAIL);
    }

    private User createUser(String email) {
        return new User(
                1L,
                email,
                "password123!",
                "Test User",
                "https://example.com/profile.jpg",
                Gender.FEMALE,
                Role.USER,
                "LOCAL",
                null,
                LocalDateTime.now().minusDays(30)
        );
    }
}
