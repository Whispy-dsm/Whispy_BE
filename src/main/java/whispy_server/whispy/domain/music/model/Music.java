package whispy_server.whispy.domain.music.model;

import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.annotation.Aggregate;

/**
 * 음악 도메인 모델 (애그리게잇).
 *
 * 수면, 집중, 명상에 사용되는 음악의 핵심 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 음악 ID
 * @param title 음악 제목
 * @param artist 아티스트/제작자
 * @param description 음악 설명
 * @param filePath 음악 파일 경로
 * @param duration 음악 길이(초)
 * @param category 음악 카테고리
 * @param bannerImageUrl 배너 이미지 URL
 * @param videoUrl 뮤직 비디오 URL
 */
@Aggregate
public record Music(
        Long id,
        String title,
        String artist,
        String description,
        String filePath,
        int duration,
        MusicCategory category,
        String bannerImageUrl,
        String videoUrl
) {

    /**
     * 음악 정보를 업데이트합니다.
     *
     * @param newTitle 새로운 제목 (null이면 기존 값 유지)
     * @param newArtist 새로운 아티스트 (null이면 기존 값 유지)
     * @param newDescription 새로운 설명 (null이면 기존 값 유지)
     * @param newFilePath 새로운 파일 경로 (null이면 기존 값 유지)
     * @param newDuration 새로운 길이 (null이면 기존 값 유지)
     * @param newCategory 새로운 카테고리 (null이면 기존 값 유지)
     * @param newBannerImageUrl 새로운 배너 이미지 URL (null이면 기존 값 유지)
     * @param newVideoUrl 새로운 뮤직 비디오 URL (null이면 기존 값 유지)
     * @return 업데이트된 음악 도메인 모델
     */
    public Music update(
            String newTitle,
            String newArtist,
            String newDescription,
            String newFilePath,
            Integer newDuration,
            MusicCategory newCategory,
            String newBannerImageUrl,
            String newVideoUrl
    ) {
        return new Music(
                this.id,
                newTitle != null ? newTitle : this.title,
                newArtist != null ? newArtist : this.artist,
                newDescription != null ? newDescription : this.description,
                newFilePath != null ? newFilePath : this.filePath,
                newDuration != null ? newDuration : this.duration,
                newCategory != null ? newCategory : this.category,
                newBannerImageUrl != null ? newBannerImageUrl : this.bannerImageUrl,
                newVideoUrl != null ? newVideoUrl : this.videoUrl
        );
    }
}