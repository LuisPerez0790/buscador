package mx.gob.scjn.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SentenceSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SentenceSearchRepositoryMockConfiguration {

    @MockBean
    private SentenceSearchRepository mockSentenceSearchRepository;

}
