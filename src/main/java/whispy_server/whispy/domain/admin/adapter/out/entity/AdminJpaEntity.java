package whispy_server.whispy.domain.admin.adapter.out.entity;

import jakarta.persistence.Entity;
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

import java.util.UUID;

@Entity(name = "AdminJpaEntity")
@Table(name = "tbl_admin")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String adminId;

    private String password;

    private Role role;

    private String name;



}
