package highcouncil.web.rest;

import highcouncil.HighCouncilApp;

import highcouncil.domain.OrderResolution;
import highcouncil.repository.OrderResolutionRepository;
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
 * Test class for the OrderResolutionResource REST controller.
 *
 * @see OrderResolutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
public class OrderResolutionResourceIntTest {

    private static final Integer DEFAULT_MINIMUM = 1;
    private static final Integer UPDATED_MINIMUM = 2;

    private static final Integer DEFAULT_MAXIMUM = 1;
    private static final Integer UPDATED_MAXIMUM = 2;

    @Autowired
    private OrderResolutionRepository orderResolutionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderResolutionMockMvc;

    private OrderResolution orderResolution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderResolutionResource orderResolutionResource = new OrderResolutionResource(orderResolutionRepository);
        this.restOrderResolutionMockMvc = MockMvcBuilders.standaloneSetup(orderResolutionResource)
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
    public static OrderResolution createEntity(EntityManager em) {
        OrderResolution orderResolution = new OrderResolution()
            .minimum(DEFAULT_MINIMUM)
            .maximum(DEFAULT_MAXIMUM);
        return orderResolution;
    }

    @Before
    public void initTest() {
        orderResolution = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderResolution() throws Exception {
        int databaseSizeBeforeCreate = orderResolutionRepository.findAll().size();

        // Create the OrderResolution
        restOrderResolutionMockMvc.perform(post("/api/order-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderResolution)))
            .andExpect(status().isCreated());

        // Validate the OrderResolution in the database
        List<OrderResolution> orderResolutionList = orderResolutionRepository.findAll();
        assertThat(orderResolutionList).hasSize(databaseSizeBeforeCreate + 1);
        OrderResolution testOrderResolution = orderResolutionList.get(orderResolutionList.size() - 1);
        assertThat(testOrderResolution.getMinimum()).isEqualTo(DEFAULT_MINIMUM);
        assertThat(testOrderResolution.getMaximum()).isEqualTo(DEFAULT_MAXIMUM);
    }

    @Test
    @Transactional
    public void createOrderResolutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderResolutionRepository.findAll().size();

        // Create the OrderResolution with an existing ID
        orderResolution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderResolutionMockMvc.perform(post("/api/order-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderResolution)))
            .andExpect(status().isBadRequest());

        // Validate the OrderResolution in the database
        List<OrderResolution> orderResolutionList = orderResolutionRepository.findAll();
        assertThat(orderResolutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrderResolutions() throws Exception {
        // Initialize the database
        orderResolutionRepository.saveAndFlush(orderResolution);

        // Get all the orderResolutionList
        restOrderResolutionMockMvc.perform(get("/api/order-resolutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderResolution.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimum").value(hasItem(DEFAULT_MINIMUM)))
            .andExpect(jsonPath("$.[*].maximum").value(hasItem(DEFAULT_MAXIMUM)));
    }

    @Test
    @Transactional
    public void getOrderResolution() throws Exception {
        // Initialize the database
        orderResolutionRepository.saveAndFlush(orderResolution);

        // Get the orderResolution
        restOrderResolutionMockMvc.perform(get("/api/order-resolutions/{id}", orderResolution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderResolution.getId().intValue()))
            .andExpect(jsonPath("$.minimum").value(DEFAULT_MINIMUM))
            .andExpect(jsonPath("$.maximum").value(DEFAULT_MAXIMUM));
    }

    @Test
    @Transactional
    public void getNonExistingOrderResolution() throws Exception {
        // Get the orderResolution
        restOrderResolutionMockMvc.perform(get("/api/order-resolutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderResolution() throws Exception {
        // Initialize the database
        orderResolutionRepository.saveAndFlush(orderResolution);
        int databaseSizeBeforeUpdate = orderResolutionRepository.findAll().size();

        // Update the orderResolution
        OrderResolution updatedOrderResolution = orderResolutionRepository.findOne(orderResolution.getId());
        updatedOrderResolution
            .minimum(UPDATED_MINIMUM)
            .maximum(UPDATED_MAXIMUM);

        restOrderResolutionMockMvc.perform(put("/api/order-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrderResolution)))
            .andExpect(status().isOk());

        // Validate the OrderResolution in the database
        List<OrderResolution> orderResolutionList = orderResolutionRepository.findAll();
        assertThat(orderResolutionList).hasSize(databaseSizeBeforeUpdate);
        OrderResolution testOrderResolution = orderResolutionList.get(orderResolutionList.size() - 1);
        assertThat(testOrderResolution.getMinimum()).isEqualTo(UPDATED_MINIMUM);
        assertThat(testOrderResolution.getMaximum()).isEqualTo(UPDATED_MAXIMUM);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderResolution() throws Exception {
        int databaseSizeBeforeUpdate = orderResolutionRepository.findAll().size();

        // Create the OrderResolution

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrderResolutionMockMvc.perform(put("/api/order-resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderResolution)))
            .andExpect(status().isCreated());

        // Validate the OrderResolution in the database
        List<OrderResolution> orderResolutionList = orderResolutionRepository.findAll();
        assertThat(orderResolutionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrderResolution() throws Exception {
        // Initialize the database
        orderResolutionRepository.saveAndFlush(orderResolution);
        int databaseSizeBeforeDelete = orderResolutionRepository.findAll().size();

        // Get the orderResolution
        restOrderResolutionMockMvc.perform(delete("/api/order-resolutions/{id}", orderResolution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderResolution> orderResolutionList = orderResolutionRepository.findAll();
        assertThat(orderResolutionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderResolution.class);
        OrderResolution orderResolution1 = new OrderResolution();
        orderResolution1.setId(1L);
        OrderResolution orderResolution2 = new OrderResolution();
        orderResolution2.setId(orderResolution1.getId());
        assertThat(orderResolution1).isEqualTo(orderResolution2);
        orderResolution2.setId(2L);
        assertThat(orderResolution1).isNotEqualTo(orderResolution2);
        orderResolution1.setId(null);
        assertThat(orderResolution1).isNotEqualTo(orderResolution2);
    }
}
