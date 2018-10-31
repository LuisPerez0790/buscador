package mx.gob.scjn.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mx.gob.scjn.domain.Sentence;
import mx.gob.scjn.domain.*; // for static metamodels
import mx.gob.scjn.repository.SentenceRepository;
import mx.gob.scjn.repository.search.SentenceSearchRepository;
import mx.gob.scjn.service.dto.SentenceCriteria;
import mx.gob.scjn.service.dto.SentenceDTO;
import mx.gob.scjn.service.mapper.SentenceMapper;

/**
 * Service for executing complex queries for Sentence entities in the database.
 * The main input is a {@link SentenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SentenceDTO} or a {@link Page} of {@link SentenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SentenceQueryService extends QueryService<Sentence> {

    private final Logger log = LoggerFactory.getLogger(SentenceQueryService.class);

    private final SentenceRepository sentenceRepository;

    private final SentenceMapper sentenceMapper;

    private final SentenceSearchRepository sentenceSearchRepository;

    public SentenceQueryService(SentenceRepository sentenceRepository, SentenceMapper sentenceMapper, SentenceSearchRepository sentenceSearchRepository) {
        this.sentenceRepository = sentenceRepository;
        this.sentenceMapper = sentenceMapper;
        this.sentenceSearchRepository = sentenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SentenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SentenceDTO> findByCriteria(SentenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sentence> specification = createSpecification(criteria);
        return sentenceMapper.toDto(sentenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SentenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SentenceDTO> findByCriteria(SentenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sentence> specification = createSpecification(criteria);
        return sentenceRepository.findAll(specification, page)
            .map(sentenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SentenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sentence> specification = createSpecification(criteria);
        return sentenceRepository.count(specification);
    }

    /**
     * Function to convert SentenceCriteria to a {@link Specification}
     */
    private Specification<Sentence> createSpecification(SentenceCriteria criteria) {
        Specification<Sentence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sentence_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Sentence_.title));
            }
            if (criteria.getGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroup(), Sentence_.group));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Sentence_.country));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Sentence_.status));
            }
            if (criteria.getEmisor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmisor(), Sentence_.emisor));
            }
            if (criteria.getFacts() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFacts(), Sentence_.facts));
            }
            if (criteria.getArgumentsSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getArgumentsSummary(), Sentence_.argumentsSummary));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), Sentence_.type));
            }
            if (criteria.getResourceId() != null) {
                specification = specification.and(buildSpecification(criteria.getResourceId(),
                    root -> root.join(Sentence_.resources, JoinType.LEFT).get(Resource_.id)));
            }
        }
        return specification;
    }
}
