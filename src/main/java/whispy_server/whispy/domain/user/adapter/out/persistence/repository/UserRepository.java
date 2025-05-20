package whispy_server.whispy.domain.user.adapter.out.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
