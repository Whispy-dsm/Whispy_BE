package whispy_server.whispy.domain.notification.application.port.out;

import java.util.List;

public interface DeleteNotificationPort {
    void deleteById(Long id);
    void deleteByEmail(String email);
    void deleteAllByIdInBatch(List<Long> ids);
}
