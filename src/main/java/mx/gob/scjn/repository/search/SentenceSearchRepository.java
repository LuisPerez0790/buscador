package mx.gob.scjn.repository.search;

import mx.gob.scjn.domain.Sentence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sentence entity.
 */
public interface SentenceSearchRepository extends ElasticsearchRepository<Sentence, Long> {
}
