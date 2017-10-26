package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.ActionResolution;

import highcouncil.repository.ActionResolutionRepository;
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
 * REST controller for managing ActionResolution.
 */
@RestController
@RequestMapping("/api")
public class ActionResolutionResource {

    private final Logger log = LoggerFactory.getLogger(ActionResolutionResource.class);

    private static final String ENTITY_NAME = "actionResolution";

    private final ActionResolutionRepository actionResolutionRepository;

    public ActionResolutionResource(ActionResolutionRepository actionResolutionRepository) {
        this.actionResolutionRepository = actionResolutionRepository;
    }

    /**
     * POST  /action-resolutions : Create a new actionResolution.
     *
     * @param actionResolution the actionResolution to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actionResolution, or with status 400 (Bad Request) if the actionResolution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/action-resolutions")
    @Timed
    public ResponseEntity<ActionResolution> createActionResolution(@RequestBody ActionResolution actionResolution) throws URISyntaxException {
        log.debug("REST request to save ActionResolution : {}", actionResolution);
        if (actionResolution.getId() != null) {
            throw new BadRequestAlertException("A new actionResolution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActionResolution result = actionResolutionRepository.save(actionResolution);
        return ResponseEntity.created(new URI("/api/action-resolutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /action-resolutions : Updates an existing actionResolution.
     *
     * @param actionResolution the actionResolution to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actionResolution,
     * or with status 400 (Bad Request) if the actionResolution is not valid,
     * or with status 500 (Internal Server Error) if the actionResolution couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/action-resolutions")
    @Timed
    public ResponseEntity<ActionResolution> updateActionResolution(@RequestBody ActionResolution actionResolution) throws URISyntaxException {
        log.debug("REST request to update ActionResolution : {}", actionResolution);
        if (actionResolution.getId() == null) {
            return createActionResolution(actionResolution);
        }
        ActionResolution result = actionResolutionRepository.save(actionResolution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actionResolution.getId().toString()))
            .body(result);
    }

    /**
     * GET  /action-resolutions : get all the actionResolutions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actionResolutions in body
     */
    @GetMapping("/action-resolutions")
    @Timed
    public List<ActionResolution> getAllActionResolutions() {
        log.debug("REST request to get all ActionResolutions");
        return actionResolutionRepository.findAll();
        }

    /**
     * GET  /action-resolutions/:id : get the "id" actionResolution.
     *
     * @param id the id of the actionResolution to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actionResolution, or with status 404 (Not Found)
     */
    @GetMapping("/action-resolutions/{id}")
    @Timed
    public ResponseEntity<ActionResolution> getActionResolution(@PathVariable Long id) {
        log.debug("REST request to get ActionResolution : {}", id);
        ActionResolution actionResolution = actionResolutionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(actionResolution));
    }

    /**
     * DELETE  /action-resolutions/:id : delete the "id" actionResolution.
     *
     * @param id the id of the actionResolution to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/action-resolutions/{id}")
    @Timed
    public ResponseEntity<Void> deleteActionResolution(@PathVariable Long id) {
        log.debug("REST request to delete ActionResolution : {}", id);
        actionResolutionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
