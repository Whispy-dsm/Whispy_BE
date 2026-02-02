package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.application.port.in.FileUploadUseCase;
import whispy_server.whispy.domain.file.application.utils.FileValidator;
import whispy_server.whispy.domain.file.application.utils.ImageCompressionConverter;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileUploadFailedException;
import whispy_server.whispy.global.file.FileProperties;
import whispy_server.whispy.global.annotation.UserAction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 파일 업로드 UseCase 구현체.
 *
 * 이미지/음원 파일 검증, 저장, URL 생성을 담당한다.
 */
@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final FileProperties fileProperties;
    private final ImageCompressionConverter imageCompressionConverter;

    /**
     * 파일을 업로드하고 접근 URL을 반환한다.
     *
     * @param file        업로드할 파일
     * @param imageFolder 저장 대상 폴더
     * @return 업로드 결과 응답
     */
    @Override
    @UserAction("파일 업로드")
    public FileUploadResponse uploadFile(MultipartFile file, ImageFolder imageFolder) {
        FileValidator.validateFile(file, imageFolder);

        String fileName = generateFileName(file, imageFolder);
        String folder = imageFolder.toString().toLowerCase();

        try {
            Files.createDirectories(Paths.get(fileProperties.uploadPath(), folder));

            if (imageFolder == ImageFolder.MUSIC_FOLDER || imageFolder == ImageFolder.MUSIC_VIDEO_FOLDER) {
                uploadOriginalFile(file, folder, fileName);
            } else if (isWebPFile(file)) {
                uploadOriginalFile(file, folder, fileName);
            } else {
                uploadCompressedImage(file, folder, fileName);
            }

            String fileUrl = generateFileUrl(folder, fileName);
            return new FileUploadResponse(fileUrl);

        } catch (IOException e) {
            throw new FileUploadFailedException(e);
        }
    }

    /**
     * 이미지를 압축하여 저장합니다.
     *
     * ImageCompressionConverter를 사용하여 이미지를 WebP 형식으로 압축한 후
     * 지정된 경로에 저장합니다.
     *
     * @param file     압축할 MultipartFile
     * @param folder   저장 폴더 경로
     * @param fileName 저장 파일명
     * @throws IOException 파일 저장 실패 시
     */
    private void uploadCompressedImage(MultipartFile file, String folder, String fileName) throws IOException {
        InputStream compressedImage = imageCompressionConverter.compressImage(file);
        Path filePath = Paths.get(fileProperties.uploadPath(), folder, fileName);
        Files.copy(compressedImage, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 원본 파일을 그대로 저장합니다.
     *
     * 음원 파일(MUSIC_FOLDER), 영상 파일(MUSIC_VIDEO_FOLDER), 또는 WebP 이미지는
     * 압축 없이 원본 그대로 저장합니다.
     *
     * @param file     저장할 MultipartFile
     * @param folder   저장 폴더 경로
     * @param fileName 저장 파일명
     * @throws IOException 파일 저장 실패 시
     */
    private void uploadOriginalFile(MultipartFile file, String folder, String fileName) throws IOException {
        Path filePath = Paths.get(fileProperties.uploadPath(), folder, fileName);
        file.transferTo(filePath.toFile());
    }

    /**
     * 저장용 파일명을 생성합니다.
     *
     * 규칙:
     * - MUSIC_FOLDER, MUSIC_VIDEO_FOLDER: UUID + 원본 확장자
     * - 나머지 폴더(이미지): UUID + ".webp"
     *
     * 예시:
     * - music.mp3 → "550e8400-e29b-41d4-a716-446655440000.mp3"
     * - profile.jpg → "550e8400-e29b-41d4-a716-446655440001.webp"
     *
     * @param file        업로드 파일
     * @param imageFolder 저장 대상 폴더
     * @return 생성된 파일명
     */
    private String generateFileName(MultipartFile file, ImageFolder imageFolder) {
        String originalFileName = file.getOriginalFilename();

        if (imageFolder == ImageFolder.MUSIC_FOLDER || imageFolder == ImageFolder.MUSIC_VIDEO_FOLDER) {
            return generateFileNameWithExtension(originalFileName);
        }

        return UUID.randomUUID().toString() + ".webp";
    }

    /**
     * 원본 확장자를 유지한 파일명을 생성합니다.
     *
     * UUID를 생성한 후, 원본 파일명에서 확장자를 추출하여 결합합니다.
     *
     * 예시:
     * - "song.mp3" → "550e8400-e29b-41d4-a716-446655440000.mp3"
     * - "video.MP4" → "550e8400-e29b-41d4-a716-446655440000.mp4" (소문자 변환)
     * - null → "550e8400-e29b-41d4-a716-446655440000" (확장자 없음)
     *
     * @param originalFileName 원본 파일명
     * @return UUID + 원본 확장자
     */
    private String generateFileNameWithExtension(String originalFileName) {
        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }

        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex).toLowerCase();
        }

        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 접근 가능한 파일 URL을 생성합니다.
     *
     * 포맷: {baseUrl}/file/{folder}/{fileName}
     *
     * 예시:
     * - "https://api.whispy.co.kr/file/profile/550e8400.webp"
     * - "https://api.whispy.co.kr/file/music_folder/song.mp3"
     *
     * @param folder   폴더 경로
     * @param fileName 파일명
     * @return 완성된 파일 URL
     */
    private String generateFileUrl(String folder, String fileName) {
        return fileProperties.baseUrl() + "/file/" + folder + "/" + fileName;
    }

    /**
     * 파일이 WebP 형식인지 확인합니다.
     *
     * 파일 확장자가 ".webp"인지 대소문자 구분 없이 검사합니다.
     * WebP 파일은 이미 최적화된 형식이므로 재압축하지 않고 원본 그대로 저장합니다.
     *
     * @param file 확인할 파일
     * @return WebP 파일이면 true, 아니면 false
     */
    private boolean isWebPFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            return false;
        }
        return originalFileName.toLowerCase().endsWith(".webp");
    }
}
