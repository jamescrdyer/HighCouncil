package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.ExpectedOrderNumber;

import highcouncil.repository.ExpectedOrderNumberRepository;
import highcouncil.web.rest.errors.BadRequestAlertException;
import highcouncil.web.rest.util.HeaderUtil;
import highcouncil.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ExpectedOrderNumbers.
 */
@RestController
@RequestMapping("/api")
public class ExpectedOrderNumberResource {

    private final Logger log = LoggerFactory.getLogger(ExpectedOrderNumberResource.class);

    private static final String ENTITY_NAME = "expectedOrderNumbers";

    private final ExpectedOrderNumberRepository expectedOrderNumbersRepository;

    public ExpectedOrderNumberResource(ExpectedOrderNumberRepository expectedOrderNumbersRepository) {
        this.expectedOrderNumbersRepository = expectedOrderNumbersRepository;
    }

    /**
     * POST  /expected-order-numbers : Create a new expectedOrderNumbers.
     *
     * @param expectedOrderNumbers the expectedOrderNumbers to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expectedOrderNumbers, or with status 400 (Bad Request) if the expectedOrderNumbers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/expected-order-numbers")
    @Timed
    public ResponseEntity<ExpectedOrderNumber> createExpectedOrderNumbers(@Valid @RequestBody ExpectedOrderNumber expectedOrderNumbers) throws URISyntaxException {
        log.debug("REST request to save ExpectedOrderNumbers : {}", expectedOrderNumbers);
        if (expectedOrderNumbers.getId() != null) {
            throw new BadRequestAlertException("A new expectedOrderNumbers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpectedOrderNumber result = expectedOrderNumbersRepository.save(expectedOrderNumbers);
        return ResponseEntity.created(new URI("/api/expected-order-numbers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expected-order-numbers : Updates an existing expectedOrderNumbers.
     *
     * @param expectedOrderNumbers the expectedOrderNumbers to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expectedOrderNumbers,
     * or with status 400 (Bad Request) if the expectedOrderNumbers is not valid,
     * or with status 500 (Internal Server Error) if the expectedOrderNumbers couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/expected-order-numbers")
    @Timed
    public ResponseEntity<ExpectedOrderNumber> updateExpectedOrderNumbers(@Valid @RequestBody ExpectedOrderNumber expectedOrderNumbers) throws URISyntaxException {
        log.debug("REST request to update ExpectedOrderNumbers : {}", expectedOrderNumbers);
        if (expectedOrderNumbers.getId() == null) {
            return createExpectedOrderNumbers(expectedOrderNumbers);
        }
        ExpectedOrderNumber result = expectedOrderNumbersRepository.save(expectedOrderNumbers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, expectedOrderNumbers.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expected-order-numbers : get all the expectedOrderNumbers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of expectedOrderNumbers in body
     */
    @GetMapping("/expected-order-numbers")
    @Timed
    public ResponseEntity<List<ExpectedOrderNumber>> getAllExpectedOrderNumbers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ExpectedOrderNumbers");
        Page<ExpectedOrderNumber> page = expectedOrderNumbersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expected-order-numbers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expected-order-numbers/:id : get the "id" expectedOrderNumbers.
     *
     * @param id the id of the expectedOrderNumbers to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expectedOrderNumbers, or with status 404 (Not Found)
     */
    @GetMapping("/expected-order-numbers/{id}")
    @Timed
    public ResponseEntity<ExpectedOrderNumber> getExpectedOrderNumbers(@PathVariable Long id) {
        log.debug("REST request to get ExpectedOrderNumbers : {}", id);
        ExpectedOrderNumber expectedOrderNumbers = expectedOrderNumbersRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(expectedOrderNumbers));
    }

    /**
     * DELETE  /expected-order-numbers/:id : delete the "id" expectedOrderNumbers.
     *
     * @param id the id of the expectedOrderNumbers to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/expected-order-numbers/{id}")
    @Timed
    public ResponseEntity<Void> deleteExpectedOrderNumbers(@PathVariable Long id) {
        log.debug("REST request to delete ExpectedOrderNumbers : {}", id);
        expectedOrderNumbersRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
