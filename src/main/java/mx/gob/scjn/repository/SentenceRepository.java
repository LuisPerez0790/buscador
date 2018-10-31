package mx.gob.scjn.repository;

import mx.gob.scjn.domain.Sentence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Sentence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long>, JpaSpecificationExecutor<Sentence> {

}
