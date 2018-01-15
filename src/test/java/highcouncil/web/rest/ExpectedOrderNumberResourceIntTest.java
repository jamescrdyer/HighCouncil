package highcouncil.web.rest;

import highcouncil.HighCouncilApp;

import highcouncil.domain.ExpectedOrderNumber;
import highcouncil.repository.ExpectedOrderNumberRepository;
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
 * Test class for the ExpectedOrderNumberResource REST controller.
 *
 * @see ExpectedOrderNumberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
public class ExpectedOrderNumberResourceIntTest {

    private static final Integer DEFAULT_NUMBER_OF_PLAYERS = 3;
    private static final Integer UPDATED_NUMBER_OF_PLAYERS = 4;

    private static final Integer DEFAULT_PLAYER_NUMBER = 1;
    private static final Integer UPDATED_PLAYER_NUMBER = 2;

    private static final Integer DEFAULT_ORDERS_EXPECTED = 1;
    private static final Integer UPDATED_ORDERS_EXPECTED = 2;

    @Autowired
    private ExpectedOrderNumberRepository expectedOrderNumbersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExpectedOrderNumberMockMvc;

    private ExpectedOrderNumber expectedOrderNumbers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExpectedOrderNumberResource expectedOrderNumbersResource = new ExpectedOrderNumberResource(expectedOrderNumbersRepository);
        this.restExpectedOrderNumberMockMvc = MockMvcBuilders.standaloneSetup(expectedOrderNumbersResource)
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
    public static ExpectedOrderNumber createEntity(EntityManager em) {
        ExpectedOrderNumber expectedOrderNumbers = new ExpectedOrderNumber()
            .numberOfPlayers(DEFAULT_NUMBER_OF_PLAYERS)
            .playerNumber(DEFAULT_PLAYER_NUMBER)
            .ordersExpected(DEFAULT_ORDERS_EXPECTED);
        return expectedOrderNumbers;
    }

    @Before
    public void initTest() {
        expectedOrderNumbers = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpectedOrderNumber() throws Exception {
        int databaseSizeBeforeCreate = expectedOrderNumbersRepository.findAll().size();

        // Create the ExpectedOrderNumber
        restExpectedOrderNumberMockMvc.perform(post("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expectedOrderNumbers)))
            .andExpect(status().isCreated());

        // Validate the ExpectedOrderNumber in the database
        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeCreate + 1);
        ExpectedOrderNumber testExpectedOrderNumber = expectedOrderNumbersList.get(expectedOrderNumbersList.size() - 1);
        assertThat(testExpectedOrderNumber.getNumberOfPlayers()).isEqualTo(DEFAULT_NUMBER_OF_PLAYERS);
        assertThat(testExpectedOrderNumber.getPlayerNumber()).isEqualTo(DEFAULT_PLAYER_NUMBER);
        assertThat(testExpectedOrderNumber.getOrdersExpected()).isEqualTo(DEFAULT_ORDERS_EXPECTED);
    }

    @Test
    @Transactional
    public void createExpectedOrderNumberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expectedOrderNumbersRepository.findAll().size();

        // Create the ExpectedOrderNumber with an existing ID
        expectedOrderNumbers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpectedOrderNumberMockMvc.perform(post("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expectedOrderNumbers)))
            .andExpect(status().isBadRequest());

        // Validate the ExpectedOrderNumber in the database
        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberOfPlayersIsRequired() throws Exception {
        int databaseSizeBeforeTest = expectedOrderNumbersRepository.findAll().size();
        // set the field null
        expectedOrderNumbers.setNumberOfPlayers(null);

        // Create the ExpectedOrderNumber, which fails.

        restExpectedOrderNumberMockMvc.perform(post("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expectedOrderNumbers)))
            .andExpect(status().isBadRequest());

        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlayerNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = expectedOrderNumbersRepository.findAll().size();
        // set the field null
        expectedOrderNumbers.setPlayerNumber(null);

        // Create the ExpectedOrderNumber, which fails.

        restExpectedOrderNumberMockMvc.perform(post("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expectedOrderNumbers)))
            .andExpect(status().isBadRequest());

        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdersExpectedIsRequired() throws Exception {
        int databaseSizeBeforeTest = expectedOrderNumbersRepository.findAll().size();
        // set the field null
        expectedOrderNumbers.setOrdersExpected(null);

        // Create the ExpectedOrderNumber, which fails.

        restExpectedOrderNumberMockMvc.perform(post("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expectedOrderNumbers)))
            .andExpect(status().isBadRequest());

        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpectedOrderNumber() throws Exception {
        // Initialize the database
        expectedOrderNumbersRepository.saveAndFlush(expectedOrderNumbers);

        // Get all the expectedOrderNumbersList
        restExpectedOrderNumberMockMvc.perform(get("/api/expected-order-numbers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expectedOrderNumbers.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberOfPlayers").value(hasItem(DEFAULT_NUMBER_OF_PLAYERS)))
            .andExpect(jsonPath("$.[*].playerNumber").value(hasItem(DEFAULT_PLAYER_NUMBER)))
            .andExpect(jsonPath("$.[*].ordersExpected").value(hasItem(DEFAULT_ORDERS_EXPECTED)));
    }

    @Test
    @Transactional
    public void getExpectedOrderNumber() throws Exception {
        // Initialize the database
        expectedOrderNumbersRepository.saveAndFlush(expectedOrderNumbers);

        // Get the expectedOrderNumbers
        restExpectedOrderNumberMockMvc.perform(get("/api/expected-order-numbers/{id}", expectedOrderNumbers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expectedOrderNumbers.getId().intValue()))
            .andExpect(jsonPath("$.numberOfPlayers").value(DEFAULT_NUMBER_OF_PLAYERS))
            .andExpect(jsonPath("$.playerNumber").value(DEFAULT_PLAYER_NUMBER))
            .andExpect(jsonPath("$.ordersExpected").value(DEFAULT_ORDERS_EXPECTED));
    }

    @Test
    @Transactional
    public void getNonExistingExpectedOrderNumber() throws Exception {
        // Get the expectedOrderNumbers
        restExpectedOrderNumberMockMvc.perform(get("/api/expected-order-numbers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpectedOrderNumber() throws Exception {
        // Initialize the database
        expectedOrderNumbersRepository.saveAndFlush(expectedOrderNumbers);
        int databaseSizeBeforeUpdate = expectedOrderNumbersRepository.findAll().size();

        // Update the expectedOrderNumbers
        ExpectedOrderNumber updatedExpectedOrderNumber = expectedOrderNumbersRepository.findOne(expectedOrderNumbers.getId());
        updatedExpectedOrderNumber
            .numberOfPlayers(UPDATED_NUMBER_OF_PLAYERS)
            .playerNumber(UPDATED_PLAYER_NUMBER)
            .ordersExpected(UPDATED_ORDERS_EXPECTED);

        restExpectedOrderNumberMockMvc.perform(put("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpectedOrderNumber)))
            .andExpect(status().isOk());

        // Validate the ExpectedOrderNumber in the database
        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeUpdate);
        ExpectedOrderNumber testExpectedOrderNumber = expectedOrderNumbersList.get(expectedOrderNumbersList.size() - 1);
        assertThat(testExpectedOrderNumber.getNumberOfPlayers()).isEqualTo(UPDATED_NUMBER_OF_PLAYERS);
        assertThat(testExpectedOrderNumber.getPlayerNumber()).isEqualTo(UPDATED_PLAYER_NUMBER);
        assertThat(testExpectedOrderNumber.getOrdersExpected()).isEqualTo(UPDATED_ORDERS_EXPECTED);
    }

    @Test
    @Transactional
    public void updateNonExistingExpectedOrderNumber() throws Exception {
        int databaseSizeBeforeUpdate = expectedOrderNumbersRepository.findAll().size();

        // Create the ExpectedOrderNumber

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExpectedOrderNumberMockMvc.perform(put("/api/expected-order-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expectedOrderNumbers)))
            .andExpect(status().isCreated());

        // Validate the ExpectedOrderNumber in the database
        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExpectedOrderNumber() throws Exception {
        // Initialize the database
        expectedOrderNumbersRepository.saveAndFlush(expectedOrderNumbers);
        int databaseSizeBeforeDelete = expectedOrderNumbersRepository.findAll().size();

        // Get the expectedOrderNumbers
        restExpectedOrderNumberMockMvc.perform(delete("/api/expected-order-numbers/{id}", expectedOrderNumbers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExpectedOrderNumber> expectedOrderNumbersList = expectedOrderNumbersRepository.findAll();
        assertThat(expectedOrderNumbersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpectedOrderNumber.class);
        ExpectedOrderNumber expectedOrderNumbers1 = new ExpectedOrderNumber();
        expectedOrderNumbers1.setId(1L);
        ExpectedOrderNumber expectedOrderNumbers2 = new ExpectedOrderNumber();
        expectedOrderNumbers2.setId(expectedOrderNumbers1.getId());
        assertThat(expectedOrderNumbers1).isEqualTo(expectedOrderNumbers2);
        expectedOrderNumbers2.setId(2L);
        assertThat(expectedOrderNumbers1).isNotEqualTo(expectedOrderNumbers2);
        expectedOrderNumbers1.setId(null);
        assertThat(expectedOrderNumbers1).isNotEqualTo(expectedOrderNumbers2);
    }
}
