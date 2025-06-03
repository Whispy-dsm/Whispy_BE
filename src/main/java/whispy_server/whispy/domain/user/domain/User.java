package whispy_server.whispy.domain.user.domain;

import java.util.UUID;

import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.domain.vo.Profile;
import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record User(
        UUID id,
        String email,
        String password,
        Profile profile,
        Role role,
        boolean calenderEnabled,
        int coin,
        String provider

) {

}

