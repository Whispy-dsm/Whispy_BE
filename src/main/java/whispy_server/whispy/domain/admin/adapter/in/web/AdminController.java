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
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.in.CreateAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.in.DeleteAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.in.UpdateAnnouncementUseCase;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.AddNewTopicRequest;
import whispy_server.whispy.domain.topic.application.port.in.AddNewTopicForAllUsersUseCase;
import whispy_server.whispy.global.document.api.admin.AdminApiDocument;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController implements AdminApiDocument {

    private final AddNewTopicForAllUsersUseCase addNewTopicForAllUsersUseCase;
    private final CreateMusicUseCase createMusicUseCase;
    private final UpdateMusicUseCase updateMusicUseCase;
    private final DeleteMusicUseCase deleteMusicUseCase;
    private final CreateAnnouncementUseCase createAnnouncementUseCase;
    private final UpdateAnnouncementUseCase updateAnnouncementUseCase;
    private final DeleteAnnouncementUseCase deleteAnnouncementUseCase;

    @PostMapping("/topics/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewTopicToAllUsers(@RequestBody @Valid AddNewTopicRequest request) {
        addNewTopicForAllUsersUseCase.execute(request.topic(), request.defaultSubscribed());
    }

    @PostMapping("/musics")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMusic(@RequestBody @Valid CreateMusicRequest request) {
        createMusicUseCase.execute(request);
    }

    @PatchMapping("/musics")
    @ResponseStatus(HttpStatus.OK)
    public void updateMusic(@RequestBody @Valid UpdateMusicRequest request) {
        updateMusicUseCase.execute(request);
    }

    @DeleteMapping("/musics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMusic(@PathVariable Long id) {
        deleteMusicUseCase.execute(id);
    }

    @PostMapping("/announcements")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAnnouncement(@RequestBody @Valid CreateAnnouncementRequest request) {
        createAnnouncementUseCase.execute(request);
    }

    @PutMapping("/announcements")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAnnouncement(@RequestBody @Valid UpdateAnnouncementRequest request) {
        updateAnnouncementUseCase.execute(request);
    }

    @DeleteMapping("/announcements/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnnouncement(@PathVariable Long id) {
        deleteAnnouncementUseCase.execute(id);
    }
}
