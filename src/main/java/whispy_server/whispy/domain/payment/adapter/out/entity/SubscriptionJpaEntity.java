package whispy_server.whispy.domain.payment.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * 구독 JPA 엔티티.
 *
 * 데이터베이스의 tbl_subscription 테이블과 매핑되는 엔티티입니다.
 * 이메일과 구독 상태에 대한 인덱스가 정의되어 있습니다.
 */
@Entity(name = "SubscriptionJpaEntity")
@Table(name = "tbl_subscription", indexes = {
        @Index(name = "idx_subscription_email", columnList = "email"),
        @Index(name = "idx_subscription_email_state", columnList = "email, subscription_state")
})
@Getter
@SuperBuilder
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