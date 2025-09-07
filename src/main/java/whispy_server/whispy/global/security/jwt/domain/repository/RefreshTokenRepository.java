package whispy_server.whispy.global.security.jwt.domain.repository;

import org.springframework.data.repository.CrudRepository;
import whispy_server.whispy.global.security.jwt.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);
}
