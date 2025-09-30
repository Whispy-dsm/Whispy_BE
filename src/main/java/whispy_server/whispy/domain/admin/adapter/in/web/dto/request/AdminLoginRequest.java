package whispy_server.whispy.domain.admin.adapter.in.web.dto.request;

public record AdminLoginRequest(
        String adminId,
        String password
) {
}
