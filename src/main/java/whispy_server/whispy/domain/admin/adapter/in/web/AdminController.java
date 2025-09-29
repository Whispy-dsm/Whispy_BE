package whispy_server.whispy.domain.admin.adapter.in.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.AddNewTopicRequest;
import whispy_server.whispy.domain.topic.application.port.in.AddNewTopicForAllUsersUseCase;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AddNewTopicForAllUsersUseCase addNewTopicForAllUsersUseCase;
    private final CreateMusicUseCase createMusicUseCase;
    private final UpdateMusicUseCase updateMusicUseCase;
    private final DeleteMusicUseCase deleteMusicUseCase;

    @PostMapping("/topics/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewTopicToAllUsers(@RequestBody @Valid AddNewTopicRequest request) {
        addNewTopicForAllUsersUseCase.execute(request.topic(), request.defaultSubscribed());
    }

    @PostMapping("/music")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMusic(@RequestBody @Valid CreateMusicRequest request) {
        createMusicUseCase.execute(request);
    }

    @PatchMapping("/music/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMusic(@RequestBody @Valid UpdateMusicRequest request) {
        updateMusicUseCase.execute(request);
    }

    @DeleteMapping("/music/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMusic(@PathVariable Long id) {
        deleteMusicUseCase.execute(id);
    }
}
