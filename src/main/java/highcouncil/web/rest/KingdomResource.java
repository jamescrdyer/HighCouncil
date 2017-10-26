package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.Kingdom;

import highcouncil.repository.KingdomRepository;
import highcouncil.web.rest.errors.BadRequestAlertException;
import highcouncil.web.rest.util.HeaderUtil;
import highcouncil.service.dto.KingdomDTO;
import highcouncil.service.mapper.KingdomMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Kingdom.
 */
@RestController
@RequestMapping("/api")
public class KingdomResource {

    private final Logger log = LoggerFactory.getLogger(KingdomResource.class);

    private static final String ENTITY_NAME = "kingdom";

    private final KingdomRepository kingdomRepository;

    private final KingdomMapper kingdomMapper;

    public KingdomResource(KingdomRepository kingdomRepository, KingdomMapper kingdomMapper) {
        this.kingdomRepository = kingdomRepository;
        this.kingdomMapper = kingdomMapper;
    }

    /**
     * POST  /kingdoms : Create a new kingdom.
     *
     * @param kingdomDTO the kingdomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new kingdomDTO, or with status 400 (Bad Request) if the kingdom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/kingdoms")
    @Timed
    public ResponseEntity<KingdomDTO> createKingdom(@Valid @RequestBody KingdomDTO kingdomDTO) throws URISyntaxException {
        log.debug("REST request to save Kingdom : {}", kingdomDTO);
        if (kingdomDTO.getId() != null) {
            throw new BadRequestAlertException("A new kingdom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kingdom kingdom = kingdomMapper.toEntity(kingdomDTO);
        kingdom = kingdomRepository.save(kingdom);
        KingdomDTO result = kingdomMapper.toDto(kingdom);
        return ResponseEntity.created(new URI("/api/kingdoms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /kingdoms : Updates an existing kingdom.
     *
     * @param kingdomDTO the kingdomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated kingdomDTO,
     * or with status 400 (Bad Request) if the kingdomDTO is not valid,
     * or with status 500 (Internal Server Error) if the kingdomDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/kingdoms")
    @Timed
    public ResponseEntity<KingdomDTO> updateKingdom(@Valid @RequestBody KingdomDTO kingdomDTO) throws URISyntaxException {
        log.debug("REST request to update Kingdom : {}", kingdomDTO);
        if (kingdomDTO.getId() == null) {
            return createKingdom(kingdomDTO);
        }
        Kingdom kingdom = kingdomMapper.toEntity(kingdomDTO);
        kingdom = kingdomRepository.save(kingdom);
        KingdomDTO result = kingdomMapper.toDto(kingdom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, kingdomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /kingdoms : get all the kingdoms.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of kingdoms in body
     */
    @GetMapping("/kingdoms")
    @Timed
    public List<KingdomDTO> getAllKingdoms(@RequestParam(required = false) String filter) {
        if ("game-is-null".equals(filter)) {
            log.debug("REST request to get all Kingdoms where game is null");
            return StreamSupport
                .stream(kingdomRepository.findAll().spliterator(), false)
                .filter(kingdom -> kingdom.getGame() == null)
                .map(kingdomMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.debug("REST request to get all Kingdoms");
        List<Kingdom> kingdoms = kingdomRepository.findAll();
        return kingdomMapper.toDto(kingdoms);
        }

    /**
     * GET  /kingdoms/:id : get the "id" kingdom.
     *
     * @param id the id of the kingdomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the kingdomDTO, or with status 404 (Not Found)
     */
    @GetMapping("/kingdoms/{id}")
    @Timed
    public ResponseEntity<KingdomDTO> getKingdom(@PathVariable Long id) {
        log.debug("REST request to get Kingdom : {}", id);
        Kingdom kingdom = kingdomRepository.findOne(id);
        KingdomDTO kingdomDTO = kingdomMapper.toDto(kingdom);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(kingdomDTO));
    }

    /**
     * DELETE  /kingdoms/:id : delete the "id" kingdom.
     *
     * @param id the id of the kingdomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/kingdoms/{id}")
    @Timed
    public ResponseEntity<Void> deleteKingdom(@PathVariable Long id) {
        log.debug("REST request to delete Kingdom : {}", id);
        kingdomRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
