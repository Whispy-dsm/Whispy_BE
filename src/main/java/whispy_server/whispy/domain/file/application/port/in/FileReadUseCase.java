package whispy_server.whispy.domain.file.application.port.in;

import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.domain.file.type.ImageFolder;

/**
 * 공개 파일 조회 유스케이스.
 */
public interface FileReadUseCase {

    /**
     * 폴더와 파일명을 기준으로 공개 파일을 조회합니다.
     *
     * @param folder 파일이 속한 도메인 폴더
     * @param fileName 조회할 파일명
     * @param byteRange HTTP Range 헤더 값. 전체 조회 시 null
     * @return 파일 스트림과 메타데이터
     */
    StoredFile readFile(ImageFolder folder, String fileName, String byteRange);
}
