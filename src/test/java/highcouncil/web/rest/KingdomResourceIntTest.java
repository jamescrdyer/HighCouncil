package highcouncil.web.rest;

import highcouncil.HighCouncilApp;

import highcouncil.domain.Kingdom;
import highcouncil.repository.KingdomRepository;
import highcouncil.service.dto.KingdomDTO;
import highcouncil.service.mapper.KingdomMapper;
import highcouncil.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the KingdomResource REST controller.
 *
 * @see KingdomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
public class KingdomResourceIntTest {

    private static final Integer DEFAULT_PIETY = 1;
    private static final Integer UPDATED_PIETY = 2;

    private static final Integer DEFAULT_POPULARITY = 1;
    private static final Integer UPDATED_POPULARITY = 2;

    private static final Integer DEFAULT_MILITARY = 1;
    private static final Integer UPDATED_MILITARY = 2;

    private static final Integer DEFAULT_WEALTH = 1;
    private static final Integer UPDATED_WEALTH = 2;

    private static final Integer DEFAULT_HEALTH = 1;
    private static final Integer UPDATED_HEALTH = 2;

    @Autowired
    private KingdomRepository kingdomRepository;

    @Autowired
    private KingdomMapper kingdomMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKingdomMockMvc;

    private Kingdom kingdom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KingdomResource kingdomResource = new KingdomResource(kingdomRepository, kingdomMapper);
        this.restKingdomMockMvc = MockMvcBuilders.standaloneSetup(kingdomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kingdom createEntity(EntityManager em) {
        Kingdom kingdom = new Kingdom()
            .piety(DEFAULT_PIETY)
            .popularity(DEFAULT_POPULARITY)
            .military(DEFAULT_MILITARY)
            .wealth(DEFAULT_WEALTH)
            .health(DEFAULT_HEALTH);
        return kingdom;
    }

    @Before
    public void initTest() {
        kingdom = createEntity(em);
    }

    @Test
    @Transactional
    public void createKingdom() throws Exception {
        int databaseSizeBeforeCreate = kingdomRepository.findAll().size();

        // Create the Kingdom
        KingdomDTO kingdomDTO = kingdomMapper.toDto(kingdom);
        restKingdomMockMvc.perform(post("/api/kingdoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kingdomDTO)))
            .andExpect(status().isCreated());

        // Validate the Kingdom in the database
        List<Kingdom> kingdomList = kingdomRepository.findAll();
        assertThat(kingdomList).hasSize(databaseSizeBeforeCreate + 1);
        Kingdom testKingdom = kingdomList.get(kingdomList.size() - 1);
        assertThat(testKingdom.getPiety()).isEqualTo(DEFAULT_PIETY);
        assertThat(testKingdom.getPopularity()).isEqualTo(DEFAULT_POPULARITY);
        assertThat(testKingdom.getMilitary()).isEqualTo(DEFAULT_MILITARY);
        assertThat(testKingdom.getWealth()).isEqualTo(DEFAULT_WEALTH);
        assertThat(testKingdom.getHealth()).isEqualTo(DEFAULT_HEALTH);
    }

    @Test
    @Transactional
    public void createKingdomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kingdomRepository.findAll().size();

        // Create the Kingdom with an existing ID
        kingdom.setId(1L);
        KingdomDTO kingdomDTO = kingdomMapper.toDto(kingdom);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKingdomMockMvc.perform(post("/api/kingdoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kingdomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Kingdom in the database
        List<Kingdom> kingdomList = kingdomRepository.findAll();
        assertThat(kingdomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHealthIsRequired() throws Exception {
        int databaseSizeBeforeTest = kingdomRepository.findAll().size();
        // set the field null
        kingdom.setHealth(null);

        // Create the Kingdom, which fails.
        KingdomDTO kingdomDTO = kingdomMapper.toDto(kingdom);

        restKingdomMockMvc.perform(post("/api/kingdoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kingdomDTO)))
            .andExpect(status().isBadRequest());

        List<Kingdom> kingdomList = kingdomRepository.findAll();
        assertThat(kingdomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKingdoms() throws Exception {
        // Initialize the database
        kingdomRepository.saveAndFlush(kingdom);

        // Get all the kingdomList
        restKingdomMockMvc.perform(get("/api/kingdoms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kingdom.getId().intValue())))
            .andExpect(jsonPath("$.[*].piety").value(hasItem(DEFAULT_PIETY)))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.[*].military").value(hasItem(DEFAULT_MILITARY)))
            .andExpect(jsonPath("$.[*].wealth").value(hasItem(DEFAULT_WEALTH)))
            .andExpect(jsonPath("$.[*].health").value(hasItem(DEFAULT_HEALTH)));
    }

    @Test
    @Transactional
    public void getKingdom() throws Exception {
        // Initialize the database
        kingdomRepository.saveAndFlush(kingdom);

        // Get the kingdom
        restKingdomMockMvc.perform(get("/api/kingdoms/{id}", kingdom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(kingdom.getId().intValue()))
            .andExpect(jsonPath("$.piety").value(DEFAULT_PIETY))
            .andExpect(jsonPath("$.popularity").value(DEFAULT_POPULARITY))
            .andExpect(jsonPath("$.military").value(DEFAULT_MILITARY))
            .andExpect(jsonPath("$.wealth").value(DEFAULT_WEALTH))
            .andExpect(jsonPath("$.health").value(DEFAULT_HEALTH));
    }

    @Test
    @Transactional
    public void getNonExistingKingdom() throws Exception {
        // Get the kingdom
        restKingdomMockMvc.perform(get("/api/kingdoms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKingdom() throws Exception {
        // Initialize the database
        kingdomRepository.saveAndFlush(kingdom);
        int databaseSizeBeforeUpdate = kingdomRepository.findAll().size();

        // Update the kingdom
        Kingdom updatedKingdom = kingdomRepository.findOne(kingdom.getId());
        updatedKingdom
            .piety(UPDATED_PIETY)
            .popularity(UPDATED_POPULARITY)
            .military(UPDATED_MILITARY)
            .wealth(UPDATED_WEALTH)
            .health(UPDATED_HEALTH);
        KingdomDTO kingdomDTO = kingdomMapper.toDto(updatedKingdom);

        restKingdomMockMvc.perform(put("/api/kingdoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kingdomDTO)))
            .andExpect(status().isOk());

        // Validate the Kingdom in the database
        List<Kingdom> kingdomList = kingdomRepository.findAll();
        assertThat(kingdomList).hasSize(databaseSizeBeforeUpdate);
        Kingdom testKingdom = kingdomList.get(kingdomList.size() - 1);
        assertThat(testKingdom.getPiety()).isEqualTo(UPDATED_PIETY);
        assertThat(testKingdom.getPopularity()).isEqualTo(UPDATED_POPULARITY);
        assertThat(testKingdom.getMilitary()).isEqualTo(UPDATED_MILITARY);
        assertThat(testKingdom.getWealth()).isEqualTo(UPDATED_WEALTH);
        assertThat(testKingdom.getHealth()).isEqualTo(UPDATED_HEALTH);
    }

    @Test
    @Transactional
    public void updateNonExistingKingdom() throws Exception {
        int databaseSizeBeforeUpdate = kingdomRepository.findAll().size();

        // Create the Kingdom
        KingdomDTO kingdomDTO = kingdomMapper.toDto(kingdom);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKingdomMockMvc.perform(put("/api/kingdoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kingdomDTO)))
            .andExpect(status().isCreated());

        // Validate the Kingdom in the database
        List<Kingdom> kingdomList = kingdomRepository.findAll();
        assertThat(kingdomList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteKingdom() throws Exception {
        // Initialize the database
        kingdomRepository.saveAndFlush(kingdom);
        int databaseSizeBeforeDelete = kingdomRepository.findAll().size();

        // Get the kingdom
        restKingdomMockMvc.perform(delete("/api/kingdoms/{id}", kingdom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Kingdom> kingdomList = kingdomRepository.findAll();
        assertThat(kingdomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kingdom.class);
        Kingdom kingdom1 = new Kingdom();
        kingdom1.setId(1L);
        Kingdom kingdom2 = new Kingdom();
        kingdom2.setId(kingdom1.getId());
        assertThat(kingdom1).isEqualTo(kingdom2);
        kingdom2.setId(2L);
        assertThat(kingdom1).isNotEqualTo(kingdom2);
        kingdom1.setId(null);
        assertThat(kingdom1).isNotEqualTo(kingdom2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KingdomDTO.class);
        KingdomDTO kingdomDTO1 = new KingdomDTO();
        kingdomDTO1.setId(1L);
        KingdomDTO kingdomDTO2 = new KingdomDTO();
        assertThat(kingdomDTO1).isNotEqualTo(kingdomDTO2);
        kingdomDTO2.setId(kingdomDTO1.getId());
        assertThat(kingdomDTO1).isEqualTo(kingdomDTO2);
        kingdomDTO2.setId(2L);
        assertThat(kingdomDTO1).isNotEqualTo(kingdomDTO2);
        kingdomDTO1.setId(null);
        assertThat(kingdomDTO1).isNotEqualTo(kingdomDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(kingdomMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(kingdomMapper.fromId(null)).isNull();
    }
}
