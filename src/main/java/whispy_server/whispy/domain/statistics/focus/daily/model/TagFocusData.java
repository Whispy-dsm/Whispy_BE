package whispy_server.whispy.domain.statistics.focus.daily.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

/**
 * 태그별 집중 데이터.
 *
 * 특정 태그에 대한 집중 통계를 나타냅니다.
 *
 * @param tag 집중 태그
 * @param minutes 태그별 집중 시간(분)
 */
public record TagFocusData(
        FocusTag tag,
        int minutes
) {
}
