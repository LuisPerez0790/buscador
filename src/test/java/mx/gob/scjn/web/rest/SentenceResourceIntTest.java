package mx.gob.scjn.web.rest;

import mx.gob.scjn.SentencesgwApp;

import mx.gob.scjn.domain.Sentence;
import mx.gob.scjn.domain.Resource;
import mx.gob.scjn.repository.SentenceRepository;
import mx.gob.scjn.repository.search.SentenceSearchRepository;
import mx.gob.scjn.service.SentenceService;
import mx.gob.scjn.service.dto.SentenceDTO;
import mx.gob.scjn.service.mapper.SentenceMapper;
import mx.gob.scjn.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.service.dto.SentenceCriteria;
import mx.gob.scjn.service.SentenceQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static mx.gob.scjn.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SentenceResource REST controller.
 *
 * @see SentenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SentencesgwApp.class)
public class SentenceResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_EMISOR = "AAAAAAAAAA";
    private static final String UPDATED_EMISOR = "BBBBBBBBBB";

    private static final String DEFAULT_FACTS = "AAAAAAAAAA";
    private static final String UPDATED_FACTS = "BBBBBBBBBB";

    private static final String DEFAULT_ARGUMENTS_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_ARGUMENTS_SUMMARY = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private SentenceMapper sentenceMapper;
    
    @Autowired
    private SentenceService sentenceService;

    /**
     * This repository is mocked in the mx.gob.scjn.repository.search test package.
     *
     * @see mx.gob.scjn.repository.search.SentenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SentenceSearchRepository mockSentenceSearchRepository;

    @Autowired
    private SentenceQueryService sentenceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSentenceMockMvc;

    private Sentence sentence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SentenceResource sentenceResource = new SentenceResource(sentenceService, sentenceQueryService);
        this.restSentenceMockMvc = MockMvcBuilders.standaloneSetup(sentenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sentence createEntity(EntityManager em) {
        Sentence sentence = new Sentence()
            .title(DEFAULT_TITLE)
            .group(DEFAULT_GROUP)
            .country(DEFAULT_COUNTRY)
            .status(DEFAULT_STATUS)
            .emisor(DEFAULT_EMISOR)
            .facts(DEFAULT_FACTS)
            .argumentsSummary(DEFAULT_ARGUMENTS_SUMMARY)
            .type(DEFAULT_TYPE);
        return sentence;
    }

    @Before
    public void initTest() {
        sentence = createEntity(em);
    }

    @Test
    @Transactional
    public void createSentence() throws Exception {
        int databaseSizeBeforeCreate = sentenceRepository.findAll().size();

        // Create the Sentence
        SentenceDTO sentenceDTO = sentenceMapper.toDto(sentence);
        restSentenceMockMvc.perform(post("/api/sentences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sentenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Sentence in the database
        List<Sentence> sentenceList = sentenceRepository.findAll();
        assertThat(sentenceList).hasSize(databaseSizeBeforeCreate + 1);
        Sentence testSentence = sentenceList.get(sentenceList.size() - 1);
        assertThat(testSentence.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSentence.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testSentence.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSentence.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSentence.getEmisor()).isEqualTo(DEFAULT_EMISOR);
        assertThat(testSentence.getFacts()).isEqualTo(DEFAULT_FACTS);
        assertThat(testSentence.getArgumentsSummary()).isEqualTo(DEFAULT_ARGUMENTS_SUMMARY);
        assertThat(testSentence.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Sentence in Elasticsearch
        verify(mockSentenceSearchRepository, times(1)).save(testSentence);
    }

    @Test
    @Transactional
    public void createSentenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sentenceRepository.findAll().size();

        // Create the Sentence with an existing ID
        sentence.setId(1L);
        SentenceDTO sentenceDTO = sentenceMapper.toDto(sentence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSentenceMockMvc.perform(post("/api/sentences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sentenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sentence in the database
        List<Sentence> sentenceList = sentenceRepository.findAll();
        assertThat(sentenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Sentence in Elasticsearch
        verify(mockSentenceSearchRepository, times(0)).save(sentence);
    }

    @Test
    @Transactional
    public void getAllSentences() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList
        restSentenceMockMvc.perform(get("/api/sentences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sentence.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emisor").value(hasItem(DEFAULT_EMISOR.toString())))
            .andExpect(jsonPath("$.[*].facts").value(hasItem(DEFAULT_FACTS.toString())))
            .andExpect(jsonPath("$.[*].argumentsSummary").value(hasItem(DEFAULT_ARGUMENTS_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getSentence() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get the sentence
        restSentenceMockMvc.perform(get("/api/sentences/{id}", sentence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sentence.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.emisor").value(DEFAULT_EMISOR.toString()))
            .andExpect(jsonPath("$.facts").value(DEFAULT_FACTS.toString()))
            .andExpect(jsonPath("$.argumentsSummary").value(DEFAULT_ARGUMENTS_SUMMARY.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getAllSentencesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where title equals to DEFAULT_TITLE
        defaultSentenceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the sentenceList where title equals to UPDATED_TITLE
        defaultSentenceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSentencesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSentenceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the sentenceList where title equals to UPDATED_TITLE
        defaultSentenceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSentencesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where title is not null
        defaultSentenceShouldBeFound("title.specified=true");

        // Get all the sentenceList where title is null
        defaultSentenceShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where group equals to DEFAULT_GROUP
        defaultSentenceShouldBeFound("group.equals=" + DEFAULT_GROUP);

        // Get all the sentenceList where group equals to UPDATED_GROUP
        defaultSentenceShouldNotBeFound("group.equals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void getAllSentencesByGroupIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where group in DEFAULT_GROUP or UPDATED_GROUP
        defaultSentenceShouldBeFound("group.in=" + DEFAULT_GROUP + "," + UPDATED_GROUP);

        // Get all the sentenceList where group equals to UPDATED_GROUP
        defaultSentenceShouldNotBeFound("group.in=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void getAllSentencesByGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where group is not null
        defaultSentenceShouldBeFound("group.specified=true");

        // Get all the sentenceList where group is null
        defaultSentenceShouldNotBeFound("group.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where country equals to DEFAULT_COUNTRY
        defaultSentenceShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the sentenceList where country equals to UPDATED_COUNTRY
        defaultSentenceShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSentencesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultSentenceShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the sentenceList where country equals to UPDATED_COUNTRY
        defaultSentenceShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSentencesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where country is not null
        defaultSentenceShouldBeFound("country.specified=true");

        // Get all the sentenceList where country is null
        defaultSentenceShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where status equals to DEFAULT_STATUS
        defaultSentenceShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the sentenceList where status equals to UPDATED_STATUS
        defaultSentenceShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSentencesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSentenceShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the sentenceList where status equals to UPDATED_STATUS
        defaultSentenceShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSentencesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where status is not null
        defaultSentenceShouldBeFound("status.specified=true");

        // Get all the sentenceList where status is null
        defaultSentenceShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByEmisorIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where emisor equals to DEFAULT_EMISOR
        defaultSentenceShouldBeFound("emisor.equals=" + DEFAULT_EMISOR);

        // Get all the sentenceList where emisor equals to UPDATED_EMISOR
        defaultSentenceShouldNotBeFound("emisor.equals=" + UPDATED_EMISOR);
    }

    @Test
    @Transactional
    public void getAllSentencesByEmisorIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where emisor in DEFAULT_EMISOR or UPDATED_EMISOR
        defaultSentenceShouldBeFound("emisor.in=" + DEFAULT_EMISOR + "," + UPDATED_EMISOR);

        // Get all the sentenceList where emisor equals to UPDATED_EMISOR
        defaultSentenceShouldNotBeFound("emisor.in=" + UPDATED_EMISOR);
    }

    @Test
    @Transactional
    public void getAllSentencesByEmisorIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where emisor is not null
        defaultSentenceShouldBeFound("emisor.specified=true");

        // Get all the sentenceList where emisor is null
        defaultSentenceShouldNotBeFound("emisor.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByFactsIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where facts equals to DEFAULT_FACTS
        defaultSentenceShouldBeFound("facts.equals=" + DEFAULT_FACTS);

        // Get all the sentenceList where facts equals to UPDATED_FACTS
        defaultSentenceShouldNotBeFound("facts.equals=" + UPDATED_FACTS);
    }

    @Test
    @Transactional
    public void getAllSentencesByFactsIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where facts in DEFAULT_FACTS or UPDATED_FACTS
        defaultSentenceShouldBeFound("facts.in=" + DEFAULT_FACTS + "," + UPDATED_FACTS);

        // Get all the sentenceList where facts equals to UPDATED_FACTS
        defaultSentenceShouldNotBeFound("facts.in=" + UPDATED_FACTS);
    }

    @Test
    @Transactional
    public void getAllSentencesByFactsIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where facts is not null
        defaultSentenceShouldBeFound("facts.specified=true");

        // Get all the sentenceList where facts is null
        defaultSentenceShouldNotBeFound("facts.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByArgumentsSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where argumentsSummary equals to DEFAULT_ARGUMENTS_SUMMARY
        defaultSentenceShouldBeFound("argumentsSummary.equals=" + DEFAULT_ARGUMENTS_SUMMARY);

        // Get all the sentenceList where argumentsSummary equals to UPDATED_ARGUMENTS_SUMMARY
        defaultSentenceShouldNotBeFound("argumentsSummary.equals=" + UPDATED_ARGUMENTS_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllSentencesByArgumentsSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where argumentsSummary in DEFAULT_ARGUMENTS_SUMMARY or UPDATED_ARGUMENTS_SUMMARY
        defaultSentenceShouldBeFound("argumentsSummary.in=" + DEFAULT_ARGUMENTS_SUMMARY + "," + UPDATED_ARGUMENTS_SUMMARY);

        // Get all the sentenceList where argumentsSummary equals to UPDATED_ARGUMENTS_SUMMARY
        defaultSentenceShouldNotBeFound("argumentsSummary.in=" + UPDATED_ARGUMENTS_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllSentencesByArgumentsSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where argumentsSummary is not null
        defaultSentenceShouldBeFound("argumentsSummary.specified=true");

        // Get all the sentenceList where argumentsSummary is null
        defaultSentenceShouldNotBeFound("argumentsSummary.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where type equals to DEFAULT_TYPE
        defaultSentenceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the sentenceList where type equals to UPDATED_TYPE
        defaultSentenceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSentencesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSentenceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the sentenceList where type equals to UPDATED_TYPE
        defaultSentenceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSentencesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where type is not null
        defaultSentenceShouldBeFound("type.specified=true");

        // Get all the sentenceList where type is null
        defaultSentenceShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllSentencesByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where type greater than or equals to DEFAULT_TYPE
        defaultSentenceShouldBeFound("type.greaterOrEqualThan=" + DEFAULT_TYPE);

        // Get all the sentenceList where type greater than or equals to UPDATED_TYPE
        defaultSentenceShouldNotBeFound("type.greaterOrEqualThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSentencesByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        // Get all the sentenceList where type less than or equals to DEFAULT_TYPE
        defaultSentenceShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the sentenceList where type less than or equals to UPDATED_TYPE
        defaultSentenceShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllSentencesByResourceIsEqualToSomething() throws Exception {
        // Initialize the database
        Resource resource = ResourceResourceIntTest.createEntity(em);
        em.persist(resource);
        em.flush();
        sentence.addResource(resource);
        sentenceRepository.saveAndFlush(sentence);
        Long resourceId = resource.getId();

        // Get all the sentenceList where resource equals to resourceId
        defaultSentenceShouldBeFound("resourceId.equals=" + resourceId);

        // Get all the sentenceList where resource equals to resourceId + 1
        defaultSentenceShouldNotBeFound("resourceId.equals=" + (resourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSentenceShouldBeFound(String filter) throws Exception {
        restSentenceMockMvc.perform(get("/api/sentences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sentence.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emisor").value(hasItem(DEFAULT_EMISOR.toString())))
            .andExpect(jsonPath("$.[*].facts").value(hasItem(DEFAULT_FACTS.toString())))
            .andExpect(jsonPath("$.[*].argumentsSummary").value(hasItem(DEFAULT_ARGUMENTS_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restSentenceMockMvc.perform(get("/api/sentences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSentenceShouldNotBeFound(String filter) throws Exception {
        restSentenceMockMvc.perform(get("/api/sentences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSentenceMockMvc.perform(get("/api/sentences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSentence() throws Exception {
        // Get the sentence
        restSentenceMockMvc.perform(get("/api/sentences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSentence() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        int databaseSizeBeforeUpdate = sentenceRepository.findAll().size();

        // Update the sentence
        Sentence updatedSentence = sentenceRepository.findById(sentence.getId()).get();
        // Disconnect from session so that the updates on updatedSentence are not directly saved in db
        em.detach(updatedSentence);
        updatedSentence
            .title(UPDATED_TITLE)
            .group(UPDATED_GROUP)
            .country(UPDATED_COUNTRY)
            .status(UPDATED_STATUS)
            .emisor(UPDATED_EMISOR)
            .facts(UPDATED_FACTS)
            .argumentsSummary(UPDATED_ARGUMENTS_SUMMARY)
            .type(UPDATED_TYPE);
        SentenceDTO sentenceDTO = sentenceMapper.toDto(updatedSentence);

        restSentenceMockMvc.perform(put("/api/sentences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sentenceDTO)))
            .andExpect(status().isOk());

        // Validate the Sentence in the database
        List<Sentence> sentenceList = sentenceRepository.findAll();
        assertThat(sentenceList).hasSize(databaseSizeBeforeUpdate);
        Sentence testSentence = sentenceList.get(sentenceList.size() - 1);
        assertThat(testSentence.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSentence.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testSentence.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSentence.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSentence.getEmisor()).isEqualTo(UPDATED_EMISOR);
        assertThat(testSentence.getFacts()).isEqualTo(UPDATED_FACTS);
        assertThat(testSentence.getArgumentsSummary()).isEqualTo(UPDATED_ARGUMENTS_SUMMARY);
        assertThat(testSentence.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Sentence in Elasticsearch
        verify(mockSentenceSearchRepository, times(1)).save(testSentence);
    }

    @Test
    @Transactional
    public void updateNonExistingSentence() throws Exception {
        int databaseSizeBeforeUpdate = sentenceRepository.findAll().size();

        // Create the Sentence
        SentenceDTO sentenceDTO = sentenceMapper.toDto(sentence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSentenceMockMvc.perform(put("/api/sentences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sentenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sentence in the database
        List<Sentence> sentenceList = sentenceRepository.findAll();
        assertThat(sentenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Sentence in Elasticsearch
        verify(mockSentenceSearchRepository, times(0)).save(sentence);
    }

    @Test
    @Transactional
    public void deleteSentence() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);

        int databaseSizeBeforeDelete = sentenceRepository.findAll().size();

        // Get the sentence
        restSentenceMockMvc.perform(delete("/api/sentences/{id}", sentence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sentence> sentenceList = sentenceRepository.findAll();
        assertThat(sentenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Sentence in Elasticsearch
        verify(mockSentenceSearchRepository, times(1)).deleteById(sentence.getId());
    }

    @Test
    @Transactional
    public void searchSentence() throws Exception {
        // Initialize the database
        sentenceRepository.saveAndFlush(sentence);
        when(mockSentenceSearchRepository.search(queryStringQuery("id:" + sentence.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sentence), PageRequest.of(0, 1), 1));
        // Search the sentence
        restSentenceMockMvc.perform(get("/api/_search/sentences?query=id:" + sentence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sentence.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emisor").value(hasItem(DEFAULT_EMISOR.toString())))
            .andExpect(jsonPath("$.[*].facts").value(hasItem(DEFAULT_FACTS.toString())))
            .andExpect(jsonPath("$.[*].argumentsSummary").value(hasItem(DEFAULT_ARGUMENTS_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sentence.class);
        Sentence sentence1 = new Sentence();
        sentence1.setId(1L);
        Sentence sentence2 = new Sentence();
        sentence2.setId(sentence1.getId());
        assertThat(sentence1).isEqualTo(sentence2);
        sentence2.setId(2L);
        assertThat(sentence1).isNotEqualTo(sentence2);
        sentence1.setId(null);
        assertThat(sentence1).isNotEqualTo(sentence2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SentenceDTO.class);
        SentenceDTO sentenceDTO1 = new SentenceDTO();
        sentenceDTO1.setId(1L);
        SentenceDTO sentenceDTO2 = new SentenceDTO();
        assertThat(sentenceDTO1).isNotEqualTo(sentenceDTO2);
        sentenceDTO2.setId(sentenceDTO1.getId());
        assertThat(sentenceDTO1).isEqualTo(sentenceDTO2);
        sentenceDTO2.setId(2L);
        assertThat(sentenceDTO1).isNotEqualTo(sentenceDTO2);
        sentenceDTO1.setId(null);
        assertThat(sentenceDTO1).isNotEqualTo(sentenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sentenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sentenceMapper.fromId(null)).isNull();
    }
}
