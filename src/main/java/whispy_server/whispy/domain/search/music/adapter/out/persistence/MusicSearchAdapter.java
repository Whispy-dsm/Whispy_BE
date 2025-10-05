package whispy_server.whispy.domain.search.music.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.search.music.adapter.out.entity.MusicElasticsearchEntity;
import whispy_server.whispy.domain.search.music.adapter.out.mapper.MusicElasticMapper;
import whispy_server.whispy.domain.search.music.adapter.out.persistence.repository.MusicElasticsearchRepository;
import whispy_server.whispy.domain.search.music.application.port.out.ElasticMusicPort;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;

@Component
@RequiredArgsConstructor
public class MusicSearchAdapter implements ElasticMusicPort {

    private final MusicElasticsearchRepository elasticsearchRepository;
    private final MusicElasticMapper musicElasticMapper;

    @Override
    public Page<Music> searchByKeyword(String keyword, Pageable pageable) {
        Page<MusicElasticsearchEntity> entities = elasticsearchRepository
                .findByTitleContaining(keyword, pageable);
        return musicElasticMapper.toMusicPage(entities);
    }

    @Override
    public Page<Music> searchByCategory(MusicCategory category, Pageable pageable) {
        Page<MusicElasticsearchEntity> entities = elasticsearchRepository
                .findByCategory(category, pageable);
        return musicElasticMapper.toMusicPage(entities);
    }

    @Override
    public void indexMusic(Music music) {
        elasticsearchRepository.save(musicElasticMapper.toElasticsearchEntity(music));
    }

    @Override
    public void deleteFromIndex(Long musicId) {
        elasticsearchRepository.deleteById(musicId.toString());
    }
}
