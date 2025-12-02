package whispy_server.whispy.domain.user.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import whispy_server.whispy.global.entity.BaseTimeEntity;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.domain.user.model.types.Gender;

/**
 * 사용자 JPA 엔티티.
 * 데이터베이스 tbl_user 테이블과 매핑되는 영속성 엔티티입니다.
 * 도메인 모델(User)과 분리되어 인프라 계층의 관심사를 처리합니다.
 */
@Entity(name = "UserJpaEntity")
@Table(name = "tbl_user")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "CHAR(70)")
    private String password;

    @Column(name = "name", columnDefinition = "CHAR(30)", nullable = false)
    private String name;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "fcm_token", nullable = true)
    private String fcmToken;

}
