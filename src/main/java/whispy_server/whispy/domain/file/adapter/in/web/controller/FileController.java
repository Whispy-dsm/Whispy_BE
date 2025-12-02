package whispy_server.whispy.domain.file.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.application.port.in.FileDeleteUseCase;
import whispy_server.whispy.domain.file.application.port.in.FileUploadUseCase;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.document.api.file.FileApiDocument;

/**
 * 파일 업로드/삭제를 처리하는 REST 컨트롤러.
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController implements FileApiDocument {

    private final FileUploadUseCase fileUploadUseCase;
    private final FileDeleteUseCase fileDeleteUseCase;

    /**
     * 멀티파트 파일을 업로드한다.
     *
     * @param file 업로드할 파일
     * @param folder 저장 폴더
     * @return 업로드 결과 응답
     */
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public FileUploadResponse uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("folder") ImageFolder folder){
        return fileUploadUseCase.uploadFile(file, folder);
    }

    /**
     * 저장된 파일을 삭제한다.
     *
     * @param folder 파일 폴더
     * @param fileName 삭제할 파일명
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@RequestParam ImageFolder folder, @RequestParam String fileName){
        fileDeleteUseCase.deleteFile(folder, fileName);
    }
}
