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

/**
 * 관리자 JPA 엔티티
 * <p>
 * 헥사고날 아키텍처의 아웃바운드 어댑터 계층에서 사용되는 영속성 엔티티입니다.
 * 데이터베이스 테이블 'tbl_admin'과 매핑되며, 관리자 정보를 저장합니다.
 * </p>
 */
@Entity(name = "AdminJpaEntity")
@Table(name = "tbl_admin")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminJpaEntity {

    /**
     * 관리자 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * 관리자 로그인 ID
     */
    private String adminId;

    /**
     * 암호화된 비밀번호
     */
    private String password;
}
