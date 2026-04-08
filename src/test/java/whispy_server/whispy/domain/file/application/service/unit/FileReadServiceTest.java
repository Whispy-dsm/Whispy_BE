package whispy_server.whispy.domain.file.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.domain.file.application.service.FileReadService;
import whispy_server.whispy.domain.file.application.utils.ImageFolderPathResolver;
import whispy_server.whispy.domain.file.type.ImageFolder;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * FileReadService의 단위 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileReadService 테스트")
class FileReadServiceTest {

    @InjectMocks
    private FileReadService service;

    @Mock
    private FileStoragePort fileStoragePort;

    @Test
    @DisplayName("폴더와 파일명으로 저장소에서 파일을 조회한다")
    void readFile_downloadsFromStorage() {
        byte[] content = "image-data".getBytes();
        StoredFile storedFile = new StoredFile(new ByteArrayInputStream(content), "image/webp", content.length);
        given(fileStoragePort.download(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, "profile.webp"), null)).willReturn(storedFile);

        StoredFile result = service.readFile(ImageFolder.PROFILE_IMAGE_FOLDER, "profile.webp", null);

        assertThat(result).isEqualTo(storedFile);
        verify(fileStoragePort).download(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, "profile.webp"), null);
    }

    @Test
    @DisplayName("range header가 있으면 저장소 조회에도 전달한다")
    void readFile_downloadsRangeFromStorage() {
        byte[] content = "image-data".getBytes();
        StoredFile storedFile = new StoredFile(new ByteArrayInputStream(content), "image/webp", content.length, true, "bytes 0-4/10");
        given(fileStoragePort.download(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, "profile.webp"), "bytes=0-4"))
                .willReturn(storedFile);

        StoredFile result = service.readFile(ImageFolder.PROFILE_IMAGE_FOLDER, "profile.webp", "bytes=0-4");

        assertThat(result).isEqualTo(storedFile);
        verify(fileStoragePort).download(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, "profile.webp"), "bytes=0-4");
    }
}
