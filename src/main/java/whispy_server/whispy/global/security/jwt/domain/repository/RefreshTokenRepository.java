package whispy_server.whispy.global.security.jwt.domain.repository;

import org.springframework.data.repository.CrudRepository;
import whispy_server.whispy.global.security.jwt.domain.entity.RefreshToken;

import java.util.Optional;

/**
 * Redis 에 저장된 리프레시 토큰을 CRUD 로 접근하기 위한 Spring Data 저장소.
 */
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    /**
     * 토큰 문자열로 엔티티를 조회해 불법 재사용 여부를 판단한다.
     *
     * @param token 클라이언트가 제시한 리프레시 토큰
     * @return Optional 형태의 조회 결과
     */
    Optional<RefreshToken> findByToken(String token);
}
