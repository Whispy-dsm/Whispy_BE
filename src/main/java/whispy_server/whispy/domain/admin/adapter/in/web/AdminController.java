package whispy_server.whispy.domain.admin.adapter.in.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.AddNewTopicRequest;
import whispy_server.whispy.domain.fcm.application.port.in.AddNewTopicForAllUsersUseCase;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AddNewTopicForAllUsersUseCase addNewTopicForAllUsersUseCase;

    @PostMapping("/fcm/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewTopicToAllUsers(@RequestBody @Valid AddNewTopicRequest request) {
        addNewTopicForAllUsersUseCase.execute(request.topic(), request.defaultSubscribed());
    }
}
