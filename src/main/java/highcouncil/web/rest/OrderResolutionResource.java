package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.OrderResolution;

import highcouncil.repository.OrderResolutionRepository;
import highcouncil.web.rest.errors.BadRequestAlertException;
import highcouncil.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrderResolution.
 */
@RestController
@RequestMapping("/api")
public class OrderResolutionResource {

    private final Logger log = LoggerFactory.getLogger(OrderResolutionResource.class);

    private static final String ENTITY_NAME = "orderResolution";

    private final OrderResolutionRepository orderResolutionRepository;

    public OrderResolutionResource(OrderResolutionRepository orderResolutionRepository) {
        this.orderResolutionRepository = orderResolutionRepository;
    }

    /**
     * POST  /order-resolutions : Create a new orderResolution.
     *
     * @param orderResolution the orderResolution to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderResolution, or with status 400 (Bad Request) if the orderResolution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-resolutions")
    @Timed
    public ResponseEntity<OrderResolution> createOrderResolution(@RequestBody OrderResolution orderResolution) throws URISyntaxException {
        log.debug("REST request to save OrderResolution : {}", orderResolution);
        if (orderResolution.getId() != null) {
            throw new BadRequestAlertException("A new orderResolution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderResolution result = orderResolutionRepository.save(orderResolution);
        return ResponseEntity.created(new URI("/api/order-resolutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-resolutions : Updates an existing orderResolution.
     *
     * @param orderResolution the orderResolution to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderResolution,
     * or with status 400 (Bad Request) if the orderResolution is not valid,
     * or with status 500 (Internal Server Error) if the orderResolution couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-resolutions")
    @Timed
    public ResponseEntity<OrderResolution> updateOrderResolution(@RequestBody OrderResolution orderResolution) throws URISyntaxException {
        log.debug("REST request to update OrderResolution : {}", orderResolution);
        if (orderResolution.getId() == null) {
            return createOrderResolution(orderResolution);
        }
        OrderResolution result = orderResolutionRepository.save(orderResolution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderResolution.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-resolutions : get all the orderResolutions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orderResolutions in body
     */
    @GetMapping("/order-resolutions")
    @Timed
    public List<OrderResolution> getAllOrderResolutions() {
        log.debug("REST request to get all OrderResolutions");
        return orderResolutionRepository.findAll();
        }

    /**
     * GET  /order-resolutions/:id : get the "id" orderResolution.
     *
     * @param id the id of the orderResolution to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderResolution, or with status 404 (Not Found)
     */
    @GetMapping("/order-resolutions/{id}")
    @Timed
    public ResponseEntity<OrderResolution> getOrderResolution(@PathVariable Long id) {
        log.debug("REST request to get OrderResolution : {}", id);
        OrderResolution orderResolution = orderResolutionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(orderResolution));
    }

    /**
     * DELETE  /order-resolutions/:id : delete the "id" orderResolution.
     *
     * @param id the id of the orderResolution to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-resolutions/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderResolution(@PathVariable Long id) {
        log.debug("REST request to delete OrderResolution : {}", id);
        orderResolutionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
