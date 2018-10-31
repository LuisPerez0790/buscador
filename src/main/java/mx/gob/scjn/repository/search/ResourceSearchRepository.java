package mx.gob.scjn.repository.search;

import mx.gob.scjn.domain.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Resource entity.
 */
public interface ResourceSearchRepository extends ElasticsearchRepository<Resource, Long> {
}
