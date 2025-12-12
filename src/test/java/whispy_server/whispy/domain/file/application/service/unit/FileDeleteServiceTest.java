package whispy_server.whispy.domain.file.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.file.application.service.FileDeleteService;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileDeleteFailedException;
import whispy_server.whispy.global.exception.domain.file.FileNotFoundException;
import whispy_server.whispy.global.file.FileProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

/**
 * FileDeleteService의 단위 테스트 클래스
 * <p>
 * 파일 삭제 서비스를 검증합니다.
 * 파일 존재 여부 확인 및 삭제 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileDeleteService 테스트")
class FileDeleteServiceTest {

    @InjectMocks
    private FileDeleteService service;

    @Mock
    private FileProperties fileProperties;

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("프로필 이미지 파일을 삭제할 수 있다")
    void deleteFile_deletesProfileImage() throws IOException {
        // given
        String folderName = "profile_image_folder";
        String fileName = "test-image.webp";

        Path folderPath = tempDir.resolve(folderName);
        Files.createDirectories(folderPath);
        Path filePath = folderPath.resolve(fileName);
        Files.createFile(filePath);

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());

        // when
        service.deleteFile(ImageFolder.PROFILE_IMAGE_FOLDER, fileName);

        // then
        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("음악 파일을 삭제할 수 있다")
    void deleteFile_deletesMusicFile() throws IOException {
        // given
        String folderName = "music_folder";
        String fileName = "test-music.mp3";

        Path folderPath = tempDir.resolve(folderName);
        Files.createDirectories(folderPath);
        Path filePath = folderPath.resolve(fileName);
        Files.createFile(filePath);

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());

        // when
        service.deleteFile(ImageFolder.MUSIC_FOLDER, fileName);

        // then
        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("음악 배너 이미지 파일을 삭제할 수 있다")
    void deleteFile_deletesMusicBannerImage() throws IOException {
        // given
        String folderName = "music_banner_image_folder";
        String fileName = "test-banner.webp";

        Path folderPath = tempDir.resolve(folderName);
        Files.createDirectories(folderPath);
        Path filePath = folderPath.resolve(fileName);
        Files.createFile(filePath);

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());

        // when
        service.deleteFile(ImageFolder.MUSIC_BANNER_IMAGE_FOLDER, fileName);

        // then
        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("파일이 존재하지 않으면 FileNotFoundException이 발생한다")
    void deleteFile_throwsException_whenFileNotFound() {
        // given
        String folderName = "profile_image_folder";
        String fileName = "non-existent-file.webp";

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());

        // when & then
        assertThrows(FileNotFoundException.class,
                () -> service.deleteFile(ImageFolder.PROFILE_IMAGE_FOLDER, fileName));
    }

    @Test
    @DisplayName("폴더가 존재하지 않으면 FileNotFoundException이 발생한다")
    void deleteFile_throwsException_whenFolderNotFound() {
        // given
        String fileName = "test-file.webp";

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());

        // when & then
        assertThrows(FileNotFoundException.class,
                () -> service.deleteFile(ImageFolder.PROFILE_IMAGE_FOLDER, fileName));
    }
}
