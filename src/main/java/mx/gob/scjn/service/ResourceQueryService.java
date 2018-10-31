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

import mx.gob.scjn.domain.Resource;
import mx.gob.scjn.domain.*; // for static metamodels
import mx.gob.scjn.repository.ResourceRepository;
import mx.gob.scjn.repository.search.ResourceSearchRepository;
import mx.gob.scjn.service.dto.ResourceCriteria;
import mx.gob.scjn.service.dto.ResourceDTO;
import mx.gob.scjn.service.mapper.ResourceMapper;

/**
 * Service for executing complex queries for Resource entities in the database.
 * The main input is a {@link ResourceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResourceDTO} or a {@link Page} of {@link ResourceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResourceQueryService extends QueryService<Resource> {

    private final Logger log = LoggerFactory.getLogger(ResourceQueryService.class);

    private final ResourceRepository resourceRepository;

    private final ResourceMapper resourceMapper;

    private final ResourceSearchRepository resourceSearchRepository;

    public ResourceQueryService(ResourceRepository resourceRepository, ResourceMapper resourceMapper, ResourceSearchRepository resourceSearchRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.resourceSearchRepository = resourceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ResourceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResourceDTO> findByCriteria(ResourceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Resource> specification = createSpecification(criteria);
        return resourceMapper.toDto(resourceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ResourceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResourceDTO> findByCriteria(ResourceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Resource> specification = createSpecification(criteria);
        return resourceRepository.findAll(specification, page)
            .map(resourceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResourceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Resource> specification = createSpecification(criteria);
        return resourceRepository.count(specification);
    }

    /**
     * Function to convert ResourceCriteria to a {@link Specification}
     */
    private Specification<Resource> createSpecification(ResourceCriteria criteria) {
        Specification<Resource> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Resource_.id));
            }
            if (criteria.getFileId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileId(), Resource_.fileId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Resource_.title));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), Resource_.path));
            }
            if (criteria.getSentenceId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentenceId(),
                    root -> root.join(Resource_.sentence, JoinType.LEFT).get(Sentence_.id)));
            }
        }
        return specification;
    }
}
