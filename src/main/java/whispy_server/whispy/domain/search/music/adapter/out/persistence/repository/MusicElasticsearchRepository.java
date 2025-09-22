package whispy_server.whispy.domain.search.music.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import whispy_server.whispy.domain.search.music.adapter.out.entity.MusicElasticsearchEntity;

public interface MusicElasticsearchRepository extends ElasticsearchRepository<MusicElasticsearchEntity, String> {
    Page<MusicElasticsearchEntity> findByTitleContaining(String title, Pageable pageable);
}