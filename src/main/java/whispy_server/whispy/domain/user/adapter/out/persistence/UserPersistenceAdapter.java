package whispy_server.whispy.domain.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.adapter.out.persistence.repository.UserRepository;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.adapter.out.mapper.UserEntityMapper;
import whispy_server.whispy.domain.user.application.port.out.UserPort;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 영속성 어댑터.
 * 헥사고날 아키텍처의 아웃바운드 어댑터로서 사용자 데이터베이스 작업을 처리합니다.
 * UserPort 인터페이스를 구현하여 도메인과 인프라를 분리합니다.
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    /**
     * 사용자를 저장합니다.
     *
     * @param user 저장할 사용자 도메인 객체
     */
    @Override
    public void save(User user){
        userRepository.save(userEntityMapper.toEntity(user));
    }

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 조회할 이메일 주소
     * @return 사용자 도메인 객체 (Optional)
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityMapper.toOptionalDomain(userRepository.findByEmail(email));
    }

    /**
     * 이메일로 사용자 존재 여부를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 사용자 존재 여부
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * ID로 사용자를 삭제합니다.
     *
     * @param id 삭제할 사용자 ID
     */
    @Override
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }
}
