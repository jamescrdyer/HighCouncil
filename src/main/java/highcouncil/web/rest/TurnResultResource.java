package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.TurnResult;

import highcouncil.repository.TurnResultRepository;
import highcouncil.web.rest.errors.BadRequestAlertException;
import highcouncil.web.rest.util.HeaderUtil;
import highcouncil.service.dto.TurnResultDTO;
import highcouncil.service.mapper.TurnResultMapper;
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
 * REST controller for managing TurnResult.
 */
@RestController
@RequestMapping("/api")
public class TurnResultResource {

    private final Logger log = LoggerFactory.getLogger(TurnResultResource.class);

    private static final String ENTITY_NAME = "turnResult";

    private final TurnResultRepository turnResultRepository;

    private final TurnResultMapper turnResultMapper;

    public TurnResultResource(TurnResultRepository turnResultRepository, TurnResultMapper turnResultMapper) {
        this.turnResultRepository = turnResultRepository;
        this.turnResultMapper = turnResultMapper;
    }

    /**
     * GET  /turn-results : get all the turnResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of turnResults in body
     */
    @GetMapping("/turn-results")
    @Timed
    public List<TurnResultDTO> getAllTurnResults() {
        log.debug("REST request to get all TurnResults");
        List<TurnResult> turnResults = turnResultRepository.findAll();
        return turnResultMapper.turnResultsToTurnResultDTOs(turnResults);
        }

    /**
     * GET  /turn-results/:id : get the "id" turnResult.
     *
     * @param id the id of the turnResultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turnResultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turn-results/{id}")
    @Timed
    public ResponseEntity<TurnResultDTO> getTurnResult(@PathVariable Long id) {
        log.debug("REST request to get TurnResult : {}", id);
        TurnResult turnResult = turnResultRepository.findOne(id);
        TurnResultDTO turnResultDTO = turnResultMapper.turnResultToTurnResultDTO(turnResult);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(turnResultDTO));
    }

    /**
     * DELETE  /turn-results/:id : delete the "id" turnResult.
     *
     * @param id the id of the turnResultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turn-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurnResult(@PathVariable Long id) {
        log.debug("REST request to delete TurnResult : {}", id);
        turnResultRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
