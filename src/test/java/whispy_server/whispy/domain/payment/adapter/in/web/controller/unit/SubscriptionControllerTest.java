package whispy_server.whispy.domain.payment.adapter.in.web.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import whispy_server.whispy.domain.payment.adapter.in.web.controller.SubscriptionController;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.SubscriptionSummaryResponse;
import whispy_server.whispy.domain.payment.application.port.in.CheckUserSubscriptionStatusUseCase;
import whispy_server.whispy.domain.payment.application.port.in.GetUserSubscriptionsUseCase;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SubscriptionController unit tests")
class SubscriptionControllerTest {

    private MockMvc mockMvc;
    private GetUserSubscriptionsUseCase getUserSubscriptionsUseCase;
    private CheckUserSubscriptionStatusUseCase checkUserSubscriptionStatusUseCase;

    @BeforeEach
    void setUp() {
        getUserSubscriptionsUseCase = Mockito.mock(GetUserSubscriptionsUseCase.class);
        checkUserSubscriptionStatusUseCase = Mockito.mock(CheckUserSubscriptionStatusUseCase.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new SubscriptionController(getUserSubscriptionsUseCase, checkUserSubscriptionStatusUseCase)
        ).build();
    }

    @Test
    @DisplayName("/subscriptions/me returns the current user's subscription without purchase token exposure")
    void getMySubscription() throws Exception {
        SubscriptionSummaryResponse subscription = new SubscriptionSummaryResponse(
                "test@example.com",
                ProductType.MONTHLY,
                LocalDateTime.of(2026, 4, 1, 0, 0),
                SubscriptionState.ACTIVE,
                true,
                LocalDateTime.of(2026, 5, 1, 0, 0)
        );
        given(getUserSubscriptionsUseCase.getUserSubscriptions())
                .willReturn(new GetUserSubscriptionsResponse(Optional.of(subscription)));

        mockMvc.perform(get("/subscriptions/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subscriptions.email").value("test@example.com"))
                .andExpect(jsonPath("$.subscriptions.productType").value("MONTHLY"))
                .andExpect(jsonPath("$.subscriptions.subscriptionState").value("ACTIVE"))
                .andExpect(jsonPath("$.subscriptions.purchaseToken").doesNotExist());
    }

    @Test
    @DisplayName("/subscriptions/me/status returns the current user's subscription status")
    void getMySubscriptionStatus() throws Exception {
        given(checkUserSubscriptionStatusUseCase.isUserSubscribed())
                .willReturn(new CheckUserSubscriptionStatusResponse(true));

        mockMvc.perform(get("/subscriptions/me/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSubscribed").value(true));
    }
}
