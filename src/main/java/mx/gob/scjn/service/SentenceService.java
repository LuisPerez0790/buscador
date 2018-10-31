package mx.gob.scjn.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.gob.scjn.domain.Sentence;
import mx.gob.scjn.repository.SentenceRepository;
import mx.gob.scjn.repository.search.SentenceSearchRepository;
import mx.gob.scjn.service.dto.FilterDTO;
import mx.gob.scjn.service.dto.SentenceDTO;
import mx.gob.scjn.service.mapper.SentenceMapper;

/**
 * Service Implementation for managing Sentence.
 */
@Service
@Transactional
public class SentenceService {

	private final Logger log = LoggerFactory.getLogger(SentenceService.class);

	private final SentenceRepository sentenceRepository;

	private final SentenceMapper sentenceMapper;

	private final SentenceSearchRepository sentenceSearchRepository;

	public SentenceService(SentenceRepository sentenceRepository, SentenceMapper sentenceMapper,
			SentenceSearchRepository sentenceSearchRepository) {
		this.sentenceRepository = sentenceRepository;
		this.sentenceMapper = sentenceMapper;
		this.sentenceSearchRepository = sentenceSearchRepository;
	}

	/**
	 * Save a sentence.
	 *
	 * @param sentenceDTO the entity to save
	 * @return the persisted entity
	 */
	public SentenceDTO save(SentenceDTO sentenceDTO) {
		log.debug("Request to save Sentence : {}", sentenceDTO);

		Sentence sentence = sentenceMapper.toEntity(sentenceDTO);
		sentence = sentenceRepository.save(sentence);
		SentenceDTO result = sentenceMapper.toDto(sentence);
		sentenceSearchRepository.save(sentence);
		return result;
	}

	/**
	 * Get all the sentences.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<SentenceDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Sentences");
		return sentenceRepository.findAll(pageable).map(sentenceMapper::toDto);
	}

	/**
	 * Get one sentence by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Optional<SentenceDTO> findOne(Long id) {
		log.debug("Request to get Sentence : {}", id);
		return sentenceRepository.findById(id).map(sentenceMapper::toDto);
	}

	/**
	 * Delete the sentence by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Sentence : {}", id);
		sentenceRepository.deleteById(id);
		sentenceSearchRepository.deleteById(id);
	}

	/**
	 * Search for the sentence corresponding to the query.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<SentenceDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of Sentences for query {}", query);
		return sentenceSearchRepository.search(queryStringQuery(query), pageable).map(sentenceMapper::toDto);
	}

	public FilterDTO getFilters() {
		List<Sentence> filters = sentenceRepository.findAll();
		FilterDTO filterDTO = new FilterDTO();
		filters.stream().forEach(item -> {
			filterDTO.getCountry().add((item.getCountry()));
			filterDTO.getGroup().add((item.getGroup()));
		});
		return filterDTO;
	}
}
