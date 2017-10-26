package highcouncil.web.rest;

import com.codahale.metrics.annotation.Timed;
import highcouncil.domain.Deck;

import highcouncil.repository.DeckRepository;
import highcouncil.web.rest.errors.BadRequestAlertException;
import highcouncil.web.rest.util.HeaderUtil;
import highcouncil.service.dto.DeckDTO;
import highcouncil.service.mapper.DeckMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Deck.
 */
@RestController
@RequestMapping("/api")
public class DeckResource {

    private final Logger log = LoggerFactory.getLogger(DeckResource.class);

    private static final String ENTITY_NAME = "deck";

    private final DeckRepository deckRepository;

    private final DeckMapper deckMapper;

    public DeckResource(DeckRepository deckRepository, DeckMapper deckMapper) {
        this.deckRepository = deckRepository;
        this.deckMapper = deckMapper;
    }

    /**
     * POST  /decks : Create a new deck.
     *
     * @param deckDTO the deckDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deckDTO, or with status 400 (Bad Request) if the deck has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/decks")
    @Timed
    public ResponseEntity<DeckDTO> createDeck(@RequestBody DeckDTO deckDTO) throws URISyntaxException {
        log.debug("REST request to save Deck : {}", deckDTO);
        if (deckDTO.getId() != null) {
            throw new BadRequestAlertException("A new deck cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deck deck = deckMapper.toEntity(deckDTO);
        deck = deckRepository.save(deck);
        DeckDTO result = deckMapper.toDto(deck);
        return ResponseEntity.created(new URI("/api/decks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /decks : Updates an existing deck.
     *
     * @param deckDTO the deckDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deckDTO,
     * or with status 400 (Bad Request) if the deckDTO is not valid,
     * or with status 500 (Internal Server Error) if the deckDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/decks")
    @Timed
    public ResponseEntity<DeckDTO> updateDeck(@RequestBody DeckDTO deckDTO) throws URISyntaxException {
        log.debug("REST request to update Deck : {}", deckDTO);
        if (deckDTO.getId() == null) {
            return createDeck(deckDTO);
        }
        Deck deck = deckMapper.toEntity(deckDTO);
        deck = deckRepository.save(deck);
        DeckDTO result = deckMapper.toDto(deck);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deckDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /decks : get all the decks.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of decks in body
     */
    @GetMapping("/decks")
    @Timed
    public List<DeckDTO> getAllDecks(@RequestParam(required = false) String filter) {
        if ("game-is-null".equals(filter)) {
            log.debug("REST request to get all Decks where game is null");
            return StreamSupport
                .stream(deckRepository.findAll().spliterator(), false)
                .filter(deck -> deck.getGame() == null)
                .map(deckMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.debug("REST request to get all Decks");
        List<Deck> decks = deckRepository.findAllWithEagerRelationships();
        return deckMapper.toDto(decks);
        }

    /**
     * GET  /decks/:id : get the "id" deck.
     *
     * @param id the id of the deckDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deckDTO, or with status 404 (Not Found)
     */
    @GetMapping("/decks/{id}")
    @Timed
    public ResponseEntity<DeckDTO> getDeck(@PathVariable Long id) {
        log.debug("REST request to get Deck : {}", id);
        Deck deck = deckRepository.findOneWithEagerRelationships(id);
        DeckDTO deckDTO = deckMapper.toDto(deck);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(deckDTO));
    }

    /**
     * DELETE  /decks/:id : delete the "id" deck.
     *
     * @param id the id of the deckDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/decks/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeck(@PathVariable Long id) {
        log.debug("REST request to delete Deck : {}", id);
        deckRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
