package whispy_server.whispy.domain.payment.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity(name = "SubscriptionJpaEntity")
@Table(name = "tbl_subscription", indexes = {
        @Index(name = "idx_subscription_email", columnList = "email"),
        @Index(name = "idx_subscription_email_state", columnList = "email, subscription_state")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "purchase_token", unique = true, nullable = false)
    private String purchaseToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "purchase_time", nullable = false)
    private LocalDateTime purchaseTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_state", nullable = false)
    private SubscriptionState subscriptionState;

    @Column(name = "auto_renewing")
    private Boolean autoRenewing;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;
}
