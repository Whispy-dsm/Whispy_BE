package whispy_server.whispy.domain.notification.application.port.out;

public interface DeleteNotificationPort {
    void deleteById(Long id);
    void deleteAllByEmail(String email);
}
