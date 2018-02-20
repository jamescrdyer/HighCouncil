package highcouncil.web.rest;

import highcouncil.HighCouncilApp;

import highcouncil.domain.TurnResult;
import highcouncil.repository.TurnResultRepository;
import highcouncil.service.dto.TurnResultDTO;
import highcouncil.service.mapper.TurnResultMapper;
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
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TurnResultResource REST controller.
 *
 * @see TurnResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
public class TurnResultResourceIntTest {

    private static final Integer DEFAULT_PIETY = 1;

    private static final Integer DEFAULT_POPULARITY = -1;

    private static final Integer DEFAULT_MILITARY = 2;

    private static final Integer DEFAULT_WEALTH = -2;

    private static final Integer DEFAULT_FAVOUR = 3;

    private static final Integer DEFAULT_TURN = 1;

    @Autowired
    private TurnResultRepository turnResultRepository;

    @Autowired
    private TurnResultMapper turnResultMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTurnResultMockMvc;

    private TurnResult turnResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TurnResultResource turnResultResource = new TurnResultResource(turnResultRepository, turnResultMapper);
        this.restTurnResultMockMvc = MockMvcBuilders.standaloneSetup(turnResultResource)
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
    public static TurnResult createEntity(EntityManager em) {
        TurnResult turnResult = new TurnResult();
        turnResult.setPiety(DEFAULT_PIETY);
        turnResult.setPopularity(DEFAULT_POPULARITY);
        turnResult.setWealth(DEFAULT_WEALTH);
        turnResult.setMilitary(DEFAULT_MILITARY);
        turnResult.setFavour(DEFAULT_FAVOUR);
        turnResult.setTurn(DEFAULT_TURN);
        return turnResult;
    }

    @Before
    public void initTest() {
        turnResult = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllTurnResults() throws Exception {
        // Initialize the database
        turnResultRepository.saveAndFlush(turnResult);

        // Get all the turnResultList
        restTurnResultMockMvc.perform(get("/api/turn-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].turn").value(hasItem(DEFAULT_TURN)));
    }

    @Test
    @Transactional
    public void getTurnResult() throws Exception {
        // Initialize the database
        turnResultRepository.saveAndFlush(turnResult);

        // Get the turnResult
        restTurnResultMockMvc.perform(get("/api/turn-results/{id}", turnResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(turnResult.getId().intValue()))
            .andExpect(jsonPath("$.summary").value(containsString("Piety "+DEFAULT_PIETY)))
            .andExpect(jsonPath("$.summary").value(containsString("Military "+DEFAULT_MILITARY)))
            .andExpect(jsonPath("$.summary").value(containsString("Popularity "+DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.summary").value(containsString("Wealth "+DEFAULT_WEALTH)))
            .andExpect(jsonPath("$.summary").value(containsString("Favour "+DEFAULT_FAVOUR)))
            .andExpect(jsonPath("$.turn").value(DEFAULT_TURN));
    }

    @Test
    @Transactional
    public void getNonExistingTurnResult() throws Exception {
        // Get the turnResult
        restTurnResultMockMvc.perform(get("/api/turn-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteTurnResult() throws Exception {
        // Initialize the database
        turnResultRepository.saveAndFlush(turnResult);
        int databaseSizeBeforeDelete = turnResultRepository.findAll().size();

        // Get the turnResult
        restTurnResultMockMvc.perform(delete("/api/turn-results/{id}", turnResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TurnResult> turnResultList = turnResultRepository.findAll();
        assertThat(turnResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnResult.class);
        TurnResult turnResult1 = new TurnResult();
        turnResult1.setId(1L);
        TurnResult turnResult2 = new TurnResult();
        turnResult2.setId(turnResult1.getId());
        assertThat(turnResult1).isEqualTo(turnResult2);
        turnResult2.setId(2L);
        assertThat(turnResult1).isNotEqualTo(turnResult2);
        turnResult1.setId(null);
        assertThat(turnResult1).isNotEqualTo(turnResult2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnResultDTO.class);
        TurnResultDTO turnResultDTO1 = new TurnResultDTO();
        turnResultDTO1.setId(1L);
        TurnResultDTO turnResultDTO2 = new TurnResultDTO();
        assertThat(turnResultDTO1).isNotEqualTo(turnResultDTO2);
        turnResultDTO2.setId(turnResultDTO1.getId());
        assertThat(turnResultDTO1).isEqualTo(turnResultDTO2);
        turnResultDTO2.setId(2L);
        assertThat(turnResultDTO1).isNotEqualTo(turnResultDTO2);
        turnResultDTO1.setId(null);
        assertThat(turnResultDTO1).isNotEqualTo(turnResultDTO2);
    }
}
