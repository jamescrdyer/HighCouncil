package highcouncil.web.rest;

import highcouncil.HighCouncilApp;

import highcouncil.domain.ActionResolution;
import highcouncil.repository.ActionResolutionRepository;
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

import highcouncil.domain.enumeration.Action;
/**
 * Test class for the ActionResolutionResource REST controller.
 *
 * @see ActionResolutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
public class ActionResolutionResourceIntTest {

    private static final Action DEFAULT_ACTION = Action.Piety;
    private static final Action UPDATED_ACTION = Action.Popularity;

    private static final String DEFAULT_CODE_NORMAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NORMAL = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_CHANCELLOR = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CHANCELLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_KINGDOM = "AAAAAAAAAA";
    private static final String UPDATED_CODE_KINGDOM = "BBBBBBBBBB";

    @Autowired
    private ActionResolutionRepository actionResolutionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActionResolutionMockMvc;

    private ActionResolution actionResolution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActionResolutionResource actionResolutionResource = new ActionResolutionResource(actionResolutionRepository);
        this.restActionResolutionMockMvc = MockMvcBuilders.standaloneSetup(actionResolutionResource)
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
    public static ActionResolution createEntity(EntityManager em) {
        ActionResolution actionResolution = new ActionResolution()
            .action(DEFAULT_ACTION)
            .codeNormal(DEFAULT_CODE_NORMAL)
            .codeChancellor(DEFAULT_CODE_CHANCELLOR)
            .codeKingdom(DEFAULT_CODE_KINGDOM);
        return actionResolution;
    }

    @Before
    public void initTest() {
        actionResolution = createEntity(em);
    }

    @Test
    @Transactional
    public void createActionResolution() throws Exception {
        int databaseSizeBeforeCreate = actionResolutionRepository.findAll().size();

        // Create the ActionResolution
        restActionResolutionMockMvc.perform(post("/api/action-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionResolution)))
            .andExpect(status().isCreated());

        // Validate the ActionResolution in the database
        List<ActionResolution> actionResolutionList = actionResolutionRepository.findAll();
        assertThat(actionResolutionList).hasSize(databaseSizeBeforeCreate + 1);
        ActionResolution testActionResolution = actionResolutionList.get(actionResolutionList.size() - 1);
        assertThat(testActionResolution.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testActionResolution.getCodeNormal()).isEqualTo(DEFAULT_CODE_NORMAL);
        assertThat(testActionResolution.getCodeChancellor()).isEqualTo(DEFAULT_CODE_CHANCELLOR);
        assertThat(testActionResolution.getCodeKingdom()).isEqualTo(DEFAULT_CODE_KINGDOM);
    }

    @Test
    @Transactional
    public void createActionResolutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actionResolutionRepository.findAll().size();

        // Create the ActionResolution with an existing ID
        actionResolution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionResolutionMockMvc.perform(post("/api/action-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionResolution)))
            .andExpect(status().isBadRequest());

        // Validate the ActionResolution in the database
        List<ActionResolution> actionResolutionList = actionResolutionRepository.findAll();
        assertThat(actionResolutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActionResolutions() throws Exception {
        // Initialize the database
        actionResolutionRepository.saveAndFlush(actionResolution);

        // Get all the actionResolutionList
        restActionResolutionMockMvc.perform(get("/api/action-resolutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actionResolution.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].codeNormal").value(hasItem(DEFAULT_CODE_NORMAL.toString())))
            .andExpect(jsonPath("$.[*].codeChancellor").value(hasItem(DEFAULT_CODE_CHANCELLOR.toString())))
            .andExpect(jsonPath("$.[*].codeKingdom").value(hasItem(DEFAULT_CODE_KINGDOM.toString())));
    }

    @Test
    @Transactional
    public void getActionResolution() throws Exception {
        // Initialize the database
        actionResolutionRepository.saveAndFlush(actionResolution);

        // Get the actionResolution
        restActionResolutionMockMvc.perform(get("/api/action-resolutions/{id}", actionResolution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actionResolution.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.codeNormal").value(DEFAULT_CODE_NORMAL.toString()))
            .andExpect(jsonPath("$.codeChancellor").value(DEFAULT_CODE_CHANCELLOR.toString()))
            .andExpect(jsonPath("$.codeKingdom").value(DEFAULT_CODE_KINGDOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActionResolution() throws Exception {
        // Get the actionResolution
        restActionResolutionMockMvc.perform(get("/api/action-resolutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActionResolution() throws Exception {
        // Initialize the database
        actionResolutionRepository.saveAndFlush(actionResolution);
        int databaseSizeBeforeUpdate = actionResolutionRepository.findAll().size();

        // Update the actionResolution
        ActionResolution updatedActionResolution = actionResolutionRepository.findOne(actionResolution.getId());
        updatedActionResolution
            .action(UPDATED_ACTION)
            .codeNormal(UPDATED_CODE_NORMAL)
            .codeChancellor(UPDATED_CODE_CHANCELLOR)
            .codeKingdom(UPDATED_CODE_KINGDOM);

        restActionResolutionMockMvc.perform(put("/api/action-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActionResolution)))
            .andExpect(status().isOk());

        // Validate the ActionResolution in the database
        List<ActionResolution> actionResolutionList = actionResolutionRepository.findAll();
        assertThat(actionResolutionList).hasSize(databaseSizeBeforeUpdate);
        ActionResolution testActionResolution = actionResolutionList.get(actionResolutionList.size() - 1);
        assertThat(testActionResolution.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testActionResolution.getCodeNormal()).isEqualTo(UPDATED_CODE_NORMAL);
        assertThat(testActionResolution.getCodeChancellor()).isEqualTo(UPDATED_CODE_CHANCELLOR);
        assertThat(testActionResolution.getCodeKingdom()).isEqualTo(UPDATED_CODE_KINGDOM);
    }

    @Test
    @Transactional
    public void updateNonExistingActionResolution() throws Exception {
        int databaseSizeBeforeUpdate = actionResolutionRepository.findAll().size();

        // Create the ActionResolution

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActionResolutionMockMvc.perform(put("/api/action-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionResolution)))
            .andExpect(status().isCreated());

        // Validate the ActionResolution in the database
        List<ActionResolution> actionResolutionList = actionResolutionRepository.findAll();
        assertThat(actionResolutionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActionResolution() throws Exception {
        // Initialize the database
        actionResolutionRepository.saveAndFlush(actionResolution);
        int databaseSizeBeforeDelete = actionResolutionRepository.findAll().size();

        // Get the actionResolution
        restActionResolutionMockMvc.perform(delete("/api/action-resolutions/{id}", actionResolution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActionResolution> actionResolutionList = actionResolutionRepository.findAll();
        assertThat(actionResolutionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionResolution.class);
        ActionResolution actionResolution1 = new ActionResolution();
        actionResolution1.setId(1L);
        ActionResolution actionResolution2 = new ActionResolution();
        actionResolution2.setId(actionResolution1.getId());
        assertThat(actionResolution1).isEqualTo(actionResolution2);
        actionResolution2.setId(2L);
        assertThat(actionResolution1).isNotEqualTo(actionResolution2);
        actionResolution1.setId(null);
        assertThat(actionResolution1).isNotEqualTo(actionResolution2);
    }
}
