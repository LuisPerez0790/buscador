package mx.gob.scjn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import mx.gob.scjn.service.SentenceQueryService;
import mx.gob.scjn.service.SentenceService;
import mx.gob.scjn.service.dto.FilterDTO;
import mx.gob.scjn.service.dto.SentenceCriteria;
import mx.gob.scjn.service.dto.SentenceDTO;
import mx.gob.scjn.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.web.rest.util.HeaderUtil;
import mx.gob.scjn.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Sentence.
 */
@RestController
@RequestMapping("/api")
public class SentenceResource {

    private final Logger log = LoggerFactory.getLogger(SentenceResource.class);

    private static final String ENTITY_NAME = "sentence";

    private final SentenceService sentenceService;

    private final SentenceQueryService sentenceQueryService;

    public SentenceResource(SentenceService sentenceService, SentenceQueryService sentenceQueryService) {
        this.sentenceService = sentenceService;
        this.sentenceQueryService = sentenceQueryService;
    }

    /**
     * POST  /sentences : Create a new sentence.
     *
     * @param sentenceDTO the sentenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sentenceDTO, or with status 400 (Bad Request) if the sentence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sentences")
    @Timed
    public ResponseEntity<SentenceDTO> createSentence(@RequestBody SentenceDTO sentenceDTO) throws URISyntaxException {
        log.debug("REST request to save Sentence : {}", sentenceDTO);
        if (sentenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new sentence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SentenceDTO result = sentenceService.save(sentenceDTO);
        return ResponseEntity.created(new URI("/api/sentences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sentences : Updates an existing sentence.
     *
     * @param sentenceDTO the sentenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sentenceDTO,
     * or with status 400 (Bad Request) if the sentenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the sentenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sentences")
    @Timed
    public ResponseEntity<SentenceDTO> updateSentence(@RequestBody SentenceDTO sentenceDTO) throws URISyntaxException {
        log.debug("REST request to update Sentence : {}", sentenceDTO);
        if (sentenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SentenceDTO result = sentenceService.save(sentenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sentenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sentences : get all the sentences.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sentences in body
     */
    @GetMapping("/sentences")
    @Timed
    public ResponseEntity<List<SentenceDTO>> getAllSentences(SentenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sentences by criteria: {}", criteria);
        Page<SentenceDTO> page = sentenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sentences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /sentences/count : count all the sentences.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/sentences/count")
    @Timed
    public ResponseEntity<Long> countSentences (SentenceCriteria criteria) {
        log.debug("REST request to count Sentences by criteria: {}", criteria);
        return ResponseEntity.ok().body(sentenceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /sentences/:id : get the "id" sentence.
     *
     * @param id the id of the sentenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sentenceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sentences/{id}")
    @Timed
    public ResponseEntity<SentenceDTO> getSentence(@PathVariable Long id) {
        log.debug("REST request to get Sentence : {}", id);
        Optional<SentenceDTO> sentenceDTO = sentenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sentenceDTO);
    }

    /**
     * DELETE  /sentences/:id : delete the "id" sentence.
     *
     * @param id the id of the sentenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sentences/{id}")
    @Timed
    public ResponseEntity<Void> deleteSentence(@PathVariable Long id) {
        log.debug("REST request to delete Sentence : {}", id);
        sentenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sentences?query=:query : search for the sentence corresponding
     * to the query.
     *
     * @param query the query of the sentence search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sentences")
    @Timed
    public ResponseEntity<List<SentenceDTO>> searchSentences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sentences for query {}", query);
        Page<SentenceDTO> page = sentenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sentences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
	@GetMapping("/sentences/filters")
	public ResponseEntity<FilterDTO> getAllfilters() {
		return new ResponseEntity<FilterDTO>(sentenceService.getFilters(), HttpStatus.OK);
	}

}
