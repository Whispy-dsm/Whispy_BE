package whispy_server.whispy.domain.file.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.service.FileDeleteService;
import whispy_server.whispy.domain.file.application.utils.ImageFolderPathResolver;
import whispy_server.whispy.domain.file.type.ImageFolder;

import static org.mockito.Mockito.verify;

/**
 * FileDeleteService의 단위 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileDeleteService 테스트")
class FileDeleteServiceTest {

    @InjectMocks
    private FileDeleteService service;

    @Mock
    private FileStoragePort fileStoragePort;

    @Test
    @DisplayName("프로필 이미지 파일을 삭제할 수 있다")
    void deleteFile_deletesProfileImage() {
        String fileName = "test-image.webp";

        service.deleteFile(ImageFolder.PROFILE_IMAGE_FOLDER, fileName);

        verify(fileStoragePort).delete(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, fileName));
    }

    @Test
    @DisplayName("음악 파일을 삭제할 수 있다")
    void deleteFile_deletesMusicFile() {
        String fileName = "test-music.mp3";

        service.deleteFile(ImageFolder.MUSIC_FOLDER, fileName);

        verify(fileStoragePort).delete(ImageFolderPathResolver.toObjectKey(ImageFolder.MUSIC_FOLDER, fileName));
    }

    @Test
    @DisplayName("음악 배너 이미지 파일을 삭제할 수 있다")
    void deleteFile_deletesMusicBannerImage() {
        String fileName = "test-banner.webp";

        service.deleteFile(ImageFolder.MUSIC_BANNER_IMAGE_FOLDER, fileName);

        verify(fileStoragePort).delete(ImageFolderPathResolver.toObjectKey(ImageFolder.MUSIC_BANNER_IMAGE_FOLDER, fileName));
    }
}
