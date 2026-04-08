package whispy_server.whispy.domain.file.application.utils;

import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileNotFoundException;

import java.util.Arrays;
import java.util.Locale;

/**
 * ImageFolder와 공개 경로 문자열 간 매핑을 담당하는 유틸리티.
 */
public final class ImageFolderPathResolver {

    private ImageFolderPathResolver() {
        throw new AssertionError("유틸리티 클래스는 객체를 생성하지 않도록 설계해야 합니다.");
    }

    /**
     * enum 값을 공개 URL/저장소 경로용 소문자 문자열로 변환합니다.
     *
     * @param folder 변환할 폴더 enum
     * @return 소문자 폴더 경로 문자열
     */
    public static String toPathName(ImageFolder folder) {
        return folder.name().toLowerCase(Locale.ROOT);
    }

    /**
     * 공개 URL에 포함된 폴더 경로 문자열을 enum으로 변환합니다.
     *
     * @param pathName 공개 URL의 폴더 경로 문자열
     * @return 매핑된 ImageFolder enum
     */
    public static ImageFolder fromPathName(String pathName) {
        return Arrays.stream(ImageFolder.values())
                .filter(folder -> toPathName(folder).equals(pathName))
                .findFirst()
                .orElseThrow(() -> FileNotFoundException.EXCEPTION);
    }

    /**
     * enum과 파일명을 이용해 저장소 object key를 생성합니다.
     *
     * @param folder 파일이 속한 폴더 enum
     * @param fileName 파일명
     * @return `{folder}/{fileName}` 형식의 object key
     */
    public static String toObjectKey(ImageFolder folder, String fileName) {
        return toPathName(folder) + "/" + fileName;
    }
}
