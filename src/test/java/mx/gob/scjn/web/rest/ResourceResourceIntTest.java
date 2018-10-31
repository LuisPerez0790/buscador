package mx.gob.scjn.web.rest;

import mx.gob.scjn.SentencesgwApp;

import mx.gob.scjn.domain.Resource;
import mx.gob.scjn.domain.Sentence;
import mx.gob.scjn.repository.ResourceRepository;
import mx.gob.scjn.repository.search.ResourceSearchRepository;
import mx.gob.scjn.service.ResourceService;
import mx.gob.scjn.service.dto.ResourceDTO;
import mx.gob.scjn.service.mapper.ResourceMapper;
import mx.gob.scjn.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.service.dto.ResourceCriteria;
import mx.gob.scjn.service.ResourceQueryService;

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
 * Test class for the ResourceResource REST controller.
 *
 * @see ResourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SentencesgwApp.class)
public class ResourceResourceIntTest {

    private static final Long DEFAULT_FILE_ID = 1L;
    private static final Long UPDATED_FILE_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;
    
    @Autowired
    private ResourceService resourceService;

    /**
     * This repository is mocked in the mx.gob.scjn.repository.search test package.
     *
     * @see mx.gob.scjn.repository.search.ResourceSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResourceSearchRepository mockResourceSearchRepository;

    @Autowired
    private ResourceQueryService resourceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResourceMockMvc;

    private Resource resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResourceResource resourceResource = new ResourceResource(resourceService, resourceQueryService);
        this.restResourceMockMvc = MockMvcBuilders.standaloneSetup(resourceResource)
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
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .fileId(DEFAULT_FILE_ID)
            .title(DEFAULT_TITLE)
            .path(DEFAULT_PATH);
        return resource;
    }

    @Before
    public void initTest() {
        resource = createEntity(em);
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getFileId()).isEqualTo(DEFAULT_FILE_ID);
        assertThat(testResource.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResource.getPath()).isEqualTo(DEFAULT_PATH);

        // Validate the Resource in Elasticsearch
        verify(mockResourceSearchRepository, times(1)).save(testResource);
    }

    @Test
    @Transactional
    public void createResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource with an existing ID
        resource.setId(1L);
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Resource in Elasticsearch
        verify(mockResourceSearchRepository, times(0)).save(resource);
    }

    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }
    
    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.fileId").value(DEFAULT_FILE_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getAllResourcesByFileIdIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where fileId equals to DEFAULT_FILE_ID
        defaultResourceShouldBeFound("fileId.equals=" + DEFAULT_FILE_ID);

        // Get all the resourceList where fileId equals to UPDATED_FILE_ID
        defaultResourceShouldNotBeFound("fileId.equals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllResourcesByFileIdIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where fileId in DEFAULT_FILE_ID or UPDATED_FILE_ID
        defaultResourceShouldBeFound("fileId.in=" + DEFAULT_FILE_ID + "," + UPDATED_FILE_ID);

        // Get all the resourceList where fileId equals to UPDATED_FILE_ID
        defaultResourceShouldNotBeFound("fileId.in=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllResourcesByFileIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where fileId is not null
        defaultResourceShouldBeFound("fileId.specified=true");

        // Get all the resourceList where fileId is null
        defaultResourceShouldNotBeFound("fileId.specified=false");
    }

    @Test
    @Transactional
    public void getAllResourcesByFileIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where fileId greater than or equals to DEFAULT_FILE_ID
        defaultResourceShouldBeFound("fileId.greaterOrEqualThan=" + DEFAULT_FILE_ID);

        // Get all the resourceList where fileId greater than or equals to UPDATED_FILE_ID
        defaultResourceShouldNotBeFound("fileId.greaterOrEqualThan=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllResourcesByFileIdIsLessThanSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where fileId less than or equals to DEFAULT_FILE_ID
        defaultResourceShouldNotBeFound("fileId.lessThan=" + DEFAULT_FILE_ID);

        // Get all the resourceList where fileId less than or equals to UPDATED_FILE_ID
        defaultResourceShouldBeFound("fileId.lessThan=" + UPDATED_FILE_ID);
    }


    @Test
    @Transactional
    public void getAllResourcesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where title equals to DEFAULT_TITLE
        defaultResourceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the resourceList where title equals to UPDATED_TITLE
        defaultResourceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllResourcesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultResourceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the resourceList where title equals to UPDATED_TITLE
        defaultResourceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllResourcesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where title is not null
        defaultResourceShouldBeFound("title.specified=true");

        // Get all the resourceList where title is null
        defaultResourceShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllResourcesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where path equals to DEFAULT_PATH
        defaultResourceShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the resourceList where path equals to UPDATED_PATH
        defaultResourceShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllResourcesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where path in DEFAULT_PATH or UPDATED_PATH
        defaultResourceShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the resourceList where path equals to UPDATED_PATH
        defaultResourceShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllResourcesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where path is not null
        defaultResourceShouldBeFound("path.specified=true");

        // Get all the resourceList where path is null
        defaultResourceShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllResourcesBySentenceIsEqualToSomething() throws Exception {
        // Initialize the database
        Sentence sentence = SentenceResourceIntTest.createEntity(em);
        em.persist(sentence);
        em.flush();
        resource.setSentence(sentence);
        resourceRepository.saveAndFlush(resource);
        Long sentenceId = sentence.getId();

        // Get all the resourceList where sentence equals to sentenceId
        defaultResourceShouldBeFound("sentenceId.equals=" + sentenceId);

        // Get all the resourceList where sentence equals to sentenceId + 1
        defaultResourceShouldNotBeFound("sentenceId.equals=" + (sentenceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultResourceShouldBeFound(String filter) throws Exception {
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));

        // Check, that the count call also returns 1
        restResourceMockMvc.perform(get("/api/resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultResourceShouldNotBeFound(String filter) throws Exception {
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceMockMvc.perform(get("/api/resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).get();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .fileId(UPDATED_FILE_ID)
            .title(UPDATED_TITLE)
            .path(UPDATED_PATH);
        ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

        restResourceMockMvc.perform(put("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getFileId()).isEqualTo(UPDATED_FILE_ID);
        assertThat(testResource.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResource.getPath()).isEqualTo(UPDATED_PATH);

        // Validate the Resource in Elasticsearch
        verify(mockResourceSearchRepository, times(1)).save(testResource);
    }

    @Test
    @Transactional
    public void updateNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc.perform(put("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resource in Elasticsearch
        verify(mockResourceSearchRepository, times(0)).save(resource);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Get the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Resource in Elasticsearch
        verify(mockResourceSearchRepository, times(1)).deleteById(resource.getId());
    }

    @Test
    @Transactional
    public void searchResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        when(mockResourceSearchRepository.search(queryStringQuery("id:" + resource.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(resource), PageRequest.of(0, 1), 1));
        // Search the resource
        restResourceMockMvc.perform(get("/api/_search/resources?query=id:" + resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resource.class);
        Resource resource1 = new Resource();
        resource1.setId(1L);
        Resource resource2 = new Resource();
        resource2.setId(resource1.getId());
        assertThat(resource1).isEqualTo(resource2);
        resource2.setId(2L);
        assertThat(resource1).isNotEqualTo(resource2);
        resource1.setId(null);
        assertThat(resource1).isNotEqualTo(resource2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceDTO.class);
        ResourceDTO resourceDTO1 = new ResourceDTO();
        resourceDTO1.setId(1L);
        ResourceDTO resourceDTO2 = new ResourceDTO();
        assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
        resourceDTO2.setId(resourceDTO1.getId());
        assertThat(resourceDTO1).isEqualTo(resourceDTO2);
        resourceDTO2.setId(2L);
        assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
        resourceDTO1.setId(null);
        assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resourceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resourceMapper.fromId(null)).isNull();
    }
}
