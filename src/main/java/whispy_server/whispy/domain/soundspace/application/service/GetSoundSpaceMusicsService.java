package whispy_server.whispy.domain.soundspace.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.SoundSpaceMusicResponse;
import whispy_server.whispy.domain.soundspace.adapter.out.dto.SoundSpaceMusicWithDetailDto;
import whispy_server.whispy.domain.soundspace.application.port.in.GetSoundSpaceMusicsUseCase;
import whispy_server.whispy.domain.soundspace.application.port.out.QuerySoundSpaceMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSoundSpaceMusicsService implements GetSoundSpaceMusicsUseCase {

    private final QuerySoundSpaceMusicPort querySoundSpaceMusicPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public List<SoundSpaceMusicResponse> execute() {
        User currentUser = userFacadeUseCase.currentUser();

        List<SoundSpaceMusicWithDetailDto> dtos =
                querySoundSpaceMusicPort.findSoundSpaceMusicsWithDetailByUserId(currentUser.id());

        return SoundSpaceMusicResponse.fromList(dtos);
    }
}
