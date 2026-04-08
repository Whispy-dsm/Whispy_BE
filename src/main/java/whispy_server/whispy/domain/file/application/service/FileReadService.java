package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.file.application.port.in.FileReadUseCase;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.domain.file.application.utils.ImageFolderPathResolver;
import whispy_server.whispy.domain.file.type.ImageFolder;

/**
 * 공개 파일 조회 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class FileReadService implements FileReadUseCase {

    private final FileStoragePort fileStoragePort;

    /**
     * 공개 URL 경로 규칙에 맞는 object key를 계산한 뒤 저장소에서 파일을 조회합니다.
     *
     * @param folder 파일이 속한 도메인 폴더
     * @param fileName 조회할 파일명
     * @param byteRange HTTP Range 헤더 원본 값
     * @return 파일 스트림과 메타데이터
     */
    @Override
    public StoredFile readFile(ImageFolder folder, String fileName, String byteRange) {
        return fileStoragePort.download(ImageFolderPathResolver.toObjectKey(folder, fileName), byteRange);
    }
}
