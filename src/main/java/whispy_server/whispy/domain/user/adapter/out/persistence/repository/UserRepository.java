package whispy_server.whispy.domain.user.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;

import java.util.Optional;

/**
 * 사용자 JPA 레포지토리.
 * Spring Data JPA를 사용하여 사용자 엔티티에 대한 데이터베이스 작업을 처리합니다.
 */
public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 조회할 이메일 주소
     * @return UserJpaEntity (Optional)
     */
    Optional<UserJpaEntity> findByEmail(String email);

    /**
     * 이메일로 사용자 존재 여부를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 사용자 존재 여부
     */
    boolean existsByEmail(String email);

}
