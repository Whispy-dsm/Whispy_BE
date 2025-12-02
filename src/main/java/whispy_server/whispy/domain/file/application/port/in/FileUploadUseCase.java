package whispy_server.whispy.domain.file.application.port.in;

import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 파일 업로드 유스케이스 계약을 정의한다.
 * 호출자는 업로드 대상 폴더와 MultipartFile 을 전달하고 업로드 결과 URL 을 응답으로 받는다.
 */
@UseCase
public interface FileUploadUseCase {

    /**
     * 파일을 지정된 폴더에 업로드한다.
     *
     * @param file        업로드할 원본 파일
     * @param imageFolder 저장할 도메인 폴더
     * @return 생성된 파일 URL 정보를 담은 응답
     */
    FileUploadResponse uploadFile(MultipartFile file, ImageFolder imageFolder);
}
