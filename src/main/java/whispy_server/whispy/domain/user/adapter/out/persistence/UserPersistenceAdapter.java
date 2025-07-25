package whispy_server.whispy.domain.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.adapter.out.persistence.repository.UserRepository;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.adapter.out.mapper.UserEntityMapper;
import whispy_server.whispy.domain.user.application.port.out.UserPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public void save(User user){
        userRepository.save(userEntityMapper.toEntity(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityMapper.toOptionalDomain(userRepository.findByEmail(email));
    }

    @Override
    public List<User> findUserAll() {
        return userRepository.findAll().stream()
                .map(userEntityMapper::toModel)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteByUUID(UUID id){
        userRepository.deleteById(id);
    }
}
