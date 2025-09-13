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
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.domain.user.model.types.Gender;


import java.util.UUID;

@Entity(name = "UserJpaEntity")
@Table(name = "tbl_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "CHAR(70)", nullable = false)
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

