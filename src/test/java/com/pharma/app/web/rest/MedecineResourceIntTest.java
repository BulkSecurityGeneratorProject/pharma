package com.pharma.app.web.rest;

import com.pharma.app.PharmaApp;

import com.pharma.app.domain.Medecine;
import com.pharma.app.repository.MedecineRepository;
import com.pharma.app.repository.search.MedecineSearchRepository;
import com.pharma.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.pharma.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MedecineResource REST controller.
 *
 * @see MedecineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PharmaApp.class)
public class MedecineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LASTUPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LASTUPDATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MedecineRepository medecineRepository;

    @Autowired
    private MedecineSearchRepository medecineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedecineMockMvc;

    private Medecine medecine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedecineResource medecineResource = new MedecineResource(medecineRepository, medecineSearchRepository);
        this.restMedecineMockMvc = MockMvcBuilders.standaloneSetup(medecineResource)
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
    public static Medecine createEntity(EntityManager em) {
        Medecine medecine = new Medecine()
            .name(DEFAULT_NAME)
            .lastupdate(DEFAULT_LASTUPDATE);
        return medecine;
    }

    @Before
    public void initTest() {
        medecineSearchRepository.deleteAll();
        medecine = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedecine() throws Exception {
        int databaseSizeBeforeCreate = medecineRepository.findAll().size();

        // Create the Medecine
        restMedecineMockMvc.perform(post("/api/medecines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecine)))
            .andExpect(status().isCreated());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeCreate + 1);
        Medecine testMedecine = medecineList.get(medecineList.size() - 1);
        assertThat(testMedecine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedecine.getLastupdate()).isEqualTo(DEFAULT_LASTUPDATE);

        // Validate the Medecine in Elasticsearch
        Medecine medecineEs = medecineSearchRepository.findOne(testMedecine.getId());
        assertThat(medecineEs).isEqualToComparingFieldByField(testMedecine);
    }

    @Test
    @Transactional
    public void createMedecineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medecineRepository.findAll().size();

        // Create the Medecine with an existing ID
        medecine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedecineMockMvc.perform(post("/api/medecines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecine)))
            .andExpect(status().isBadRequest());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medecineRepository.findAll().size();
        // set the field null
        medecine.setName(null);

        // Create the Medecine, which fails.

        restMedecineMockMvc.perform(post("/api/medecines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecine)))
            .andExpect(status().isBadRequest());

        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedecines() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        // Get all the medecineList
        restMedecineMockMvc.perform(get("/api/medecines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastupdate").value(hasItem(DEFAULT_LASTUPDATE.toString())));
    }

    @Test
    @Transactional
    public void getMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        // Get the medecine
        restMedecineMockMvc.perform(get("/api/medecines/{id}", medecine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medecine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastupdate").value(DEFAULT_LASTUPDATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedecine() throws Exception {
        // Get the medecine
        restMedecineMockMvc.perform(get("/api/medecines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);
        medecineSearchRepository.save(medecine);
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();

        // Update the medecine
        Medecine updatedMedecine = medecineRepository.findOne(medecine.getId());
        updatedMedecine
            .name(UPDATED_NAME)
            .lastupdate(UPDATED_LASTUPDATE);

        restMedecineMockMvc.perform(put("/api/medecines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedecine)))
            .andExpect(status().isOk());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
        Medecine testMedecine = medecineList.get(medecineList.size() - 1);
        assertThat(testMedecine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedecine.getLastupdate()).isEqualTo(UPDATED_LASTUPDATE);

        // Validate the Medecine in Elasticsearch
        Medecine medecineEs = medecineSearchRepository.findOne(testMedecine.getId());
        assertThat(medecineEs).isEqualToComparingFieldByField(testMedecine);
    }

    @Test
    @Transactional
    public void updateNonExistingMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();

        // Create the Medecine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedecineMockMvc.perform(put("/api/medecines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecine)))
            .andExpect(status().isCreated());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);
        medecineSearchRepository.save(medecine);
        int databaseSizeBeforeDelete = medecineRepository.findAll().size();

        // Get the medecine
        restMedecineMockMvc.perform(delete("/api/medecines/{id}", medecine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean medecineExistsInEs = medecineSearchRepository.exists(medecine.getId());
        assertThat(medecineExistsInEs).isFalse();

        // Validate the database is empty
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);
        medecineSearchRepository.save(medecine);

        // Search the medecine
        restMedecineMockMvc.perform(get("/api/_search/medecines?query=id:" + medecine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastupdate").value(hasItem(DEFAULT_LASTUPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medecine.class);
        Medecine medecine1 = new Medecine();
        medecine1.setId(1L);
        Medecine medecine2 = new Medecine();
        medecine2.setId(medecine1.getId());
        assertThat(medecine1).isEqualTo(medecine2);
        medecine2.setId(2L);
        assertThat(medecine1).isNotEqualTo(medecine2);
        medecine1.setId(null);
        assertThat(medecine1).isNotEqualTo(medecine2);
    }
}
