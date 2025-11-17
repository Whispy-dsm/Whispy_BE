package whispy_server.whispy.domain.meditationsession.application.port.out;

public interface DeleteMeditationSessionPort {
    void deleteById(Long id);
    void deleteByUserId(Long userId);
}
