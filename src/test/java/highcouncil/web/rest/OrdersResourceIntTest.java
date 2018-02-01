package highcouncil.web.rest;

import highcouncil.HighCouncilApp;
import highcouncil.domain.Game;
import highcouncil.domain.Orders;
import highcouncil.domain.Player;
import highcouncil.repository.GameRepository;
import highcouncil.repository.OrdersRepository;
import highcouncil.repository.PlayerRepository;
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
 * Test class for the OrdersResource REST controller.
 *
 * @see OrdersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
public class OrdersResourceIntTest {

    private static final Integer DEFAULT_TURN = 1;
    private static final Integer UPDATED_TURN = 2;

    private static final Integer DEFAULT_PIETY = 1;
    private static final Integer UPDATED_PIETY = 2;

    private static final Integer DEFAULT_POPULARITY = 1;
    private static final Integer UPDATED_POPULARITY = 2;

    private static final Integer DEFAULT_MILITARY = 1;
    private static final Integer UPDATED_MILITARY = 2;

    private static final Integer DEFAULT_WEALTH = 1;
    private static final Integer UPDATED_WEALTH = 2;

    private static final Integer DEFAULT_FAVOUR = 1;
    private static final Integer UPDATED_FAVOUR = 2;

    private static final Action DEFAULT_ACTION = Action.Piety;
    private static final Action UPDATED_ACTION = Action.Popularity;

    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersMockMvc;

    private Orders orders;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersResource ordersResource = new OrdersResource(ordersRepository);
        this.restOrdersMockMvc = MockMvcBuilders.standaloneSetup(ordersResource)
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
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
            .turn(DEFAULT_TURN)
            .piety(DEFAULT_PIETY)
            .popularity(DEFAULT_POPULARITY)
            .military(DEFAULT_MILITARY)
            .wealth(DEFAULT_WEALTH)
            .favour(DEFAULT_FAVOUR)
            .action(DEFAULT_ACTION);
        return orders;
    }

    @Before
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrders() throws Exception {
    	setupGame();
    	setupPlayer();
    	
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getTurn()).isEqualTo(DEFAULT_TURN);
        assertThat(testOrders.getPiety()).isEqualTo(DEFAULT_PIETY);
        assertThat(testOrders.getPopularity()).isEqualTo(DEFAULT_POPULARITY);
        assertThat(testOrders.getMilitary()).isEqualTo(DEFAULT_MILITARY);
        assertThat(testOrders.getWealth()).isEqualTo(DEFAULT_WEALTH);
        assertThat(testOrders.getFavour()).isEqualTo(DEFAULT_FAVOUR);
        assertThat(testOrders.getAction()).isEqualTo(DEFAULT_ACTION);
    }

	private void setupGame() {
    	Game game = GameResourceIntTest.createEntity(em);
    	game = gameRepository.save(game);
    	orders.setGame(game);
	}

	private void setupPlayer() {
		Player player = PlayerResourceIntTest.createEntity(em);
    	player = playerRepository.save(player);
    	orders.setPlayer(player);
	}

    @Test
    @Transactional
    public void createOrdersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders with an existing ID
        orders.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTurnIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setTurn(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPietyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setPiety(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPopularityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setPopularity(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMilitaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setMilitary(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWealthIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setWealth(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFavourIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setFavour(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].turn").value(hasItem(DEFAULT_TURN)))
            .andExpect(jsonPath("$.[*].piety").value(hasItem(DEFAULT_PIETY)))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.[*].military").value(hasItem(DEFAULT_MILITARY)))
            .andExpect(jsonPath("$.[*].wealth").value(hasItem(DEFAULT_WEALTH)))
            .andExpect(jsonPath("$.[*].favour").value(hasItem(DEFAULT_FAVOUR)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }

    @Test
    @Transactional
    public void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.turn").value(DEFAULT_TURN))
            .andExpect(jsonPath("$.piety").value(DEFAULT_PIETY))
            .andExpect(jsonPath("$.popularity").value(DEFAULT_POPULARITY))
            .andExpect(jsonPath("$.military").value(DEFAULT_MILITARY))
            .andExpect(jsonPath("$.wealth").value(DEFAULT_WEALTH))
            .andExpect(jsonPath("$.favour").value(DEFAULT_FAVOUR))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findOne(orders.getId());
        updatedOrders
            .turn(UPDATED_TURN)
            .piety(UPDATED_PIETY)
            .popularity(UPDATED_POPULARITY)
            .military(UPDATED_MILITARY)
            .wealth(UPDATED_WEALTH)
            .favour(UPDATED_FAVOUR)
            .action(UPDATED_ACTION);

        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrders)))
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getTurn()).isEqualTo(UPDATED_TURN);
        assertThat(testOrders.getPiety()).isEqualTo(UPDATED_PIETY);
        assertThat(testOrders.getPopularity()).isEqualTo(UPDATED_POPULARITY);
        assertThat(testOrders.getMilitary()).isEqualTo(UPDATED_MILITARY);
        assertThat(testOrders.getWealth()).isEqualTo(UPDATED_WEALTH);
        assertThat(testOrders.getFavour()).isEqualTo(UPDATED_FAVOUR);
        assertThat(testOrders.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    public void updateNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        setupGame();
    	setupPlayer();

        // Create the Orders

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);
        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Get the orders
        restOrdersMockMvc.perform(delete("/api/orders/{id}", orders.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orders.class);
        Orders orders1 = new Orders();
        orders1.setId(1L);
        Orders orders2 = new Orders();
        orders2.setId(orders1.getId());
        assertThat(orders1).isEqualTo(orders2);
        orders2.setId(2L);
        assertThat(orders1).isNotEqualTo(orders2);
        orders1.setId(null);
        assertThat(orders1).isNotEqualTo(orders2);
    }
}
