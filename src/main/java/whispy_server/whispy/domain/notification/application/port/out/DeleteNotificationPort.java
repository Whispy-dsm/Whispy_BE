package whispy_server.whispy.domain.notification.application.port.out;

import java.util.List;

public interface DeleteNotificationPort {
    void deleteById(Long id);
    void deleteAllByEmail(String email);
    long deleteAllByIdInBatch(List<Long> ids);
    long deleteAllByEmailBatch(String email);
}
