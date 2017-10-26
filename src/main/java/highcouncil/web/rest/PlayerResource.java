package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.Player;

import highcouncil.repository.PlayerRepository;
import highcouncil.web.rest.errors.BadRequestAlertException;
import highcouncil.web.rest.util.HeaderUtil;
import highcouncil.service.dto.PlayerDTO;
import highcouncil.service.mapper.PlayerMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Player.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "player";

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    public PlayerResource(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    /**
     * POST  /players : Create a new player.
     *
     * @param playerDTO the playerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new playerDTO, or with status 400 (Bad Request) if the player has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/players")
    @Timed
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
        log.debug("REST request to save Player : {}", playerDTO);
        if (playerDTO.getId() != null) {
            throw new BadRequestAlertException("A new player cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        PlayerDTO result = playerMapper.toDto(player);
        return ResponseEntity.created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /players : Updates an existing player.
     *
     * @param playerDTO the playerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated playerDTO,
     * or with status 400 (Bad Request) if the playerDTO is not valid,
     * or with status 500 (Internal Server Error) if the playerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/players")
    @Timed
    public ResponseEntity<PlayerDTO> updatePlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
        log.debug("REST request to update Player : {}", playerDTO);
        if (playerDTO.getId() == null) {
            return createPlayer(playerDTO);
        }
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        PlayerDTO result = playerMapper.toDto(player);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, playerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /players : get all the players.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of players in body
     */
    @GetMapping("/players")
    @Timed
    public List<PlayerDTO> getAllPlayers() {
        log.debug("REST request to get all Players");
        List<Player> players = playerRepository.findAllWithEagerRelationships();
        return playerMapper.toDto(players);
        }

    /**
     * GET  /players/:id : get the "id" player.
     *
     * @param id the id of the playerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the playerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/players/{id}")
    @Timed
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long id) {
        log.debug("REST request to get Player : {}", id);
        Player player = playerRepository.findOneWithEagerRelationships(id);
        PlayerDTO playerDTO = playerMapper.toDto(player);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(playerDTO));
    }

    /**
     * DELETE  /players/:id : delete the "id" player.
     *
     * @param id the id of the playerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/players/{id}")
    @Timed
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.debug("REST request to delete Player : {}", id);
        playerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
