package whispy_server.whispy.domain.auth.adapter.out.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import whispy_server.whispy.domain.auth.adapter.out.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);
}
