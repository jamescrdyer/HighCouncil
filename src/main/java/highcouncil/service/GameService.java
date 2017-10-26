package highcouncil.service;

import highcouncil.domain.Game;
import highcouncil.domain.Kingdom;
import highcouncil.domain.Player;
import highcouncil.domain.User;
import highcouncil.repository.GameRepository;
import highcouncil.service.dto.GameDTO;
import highcouncil.service.mapper.GameMapper;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final UserService userService;

    private final GameMapper gameMapper;

    public GameService(GameRepository gameRepository, GameMapper gameMapper, UserService userService) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.userService = userService;
    }

    /**
     * Save a game.
     *
     * @param gameDTO the entity to save
     * @return the persisted entity
     */
    public GameDTO save(GameDTO gameDTO) {
        log.debug("Request to save Game : {}", gameDTO);
        Game game = gameMapper.toEntity(gameDTO);
        game = gameRepository.save(game);
        if (game.getKingdom() == null) {
        	addKingdom(game);
        }
        if (game.getPlayers().isEmpty()) {
        	addPlayer(game, "user");
        	addPlayer(game, "admin");
        }
        return gameMapper.toDto(game);
    }

	public Player addPlayer(Game game, String login) {
		Player p = new Player();
		p.setFavour(2);
		p.setMilitary(2);
		p.setPiety(2);
		p.setPopularity(2);
		p.setWealth(2);
		Optional<User> optUser = userService.getUserWithAuthoritiesByLogin(login);
		if (optUser.isPresent()) {
			User u = optUser.get();
			p.setUser(u);
			p.setName(u.getLogin());
			p.setGame(game);
	    	game.getPlayers().add(p);
	    	return p;
		}
		return null;
	}
    
    private void addKingdom(Game game) {
    	Kingdom k = new Kingdom();
    	k.setHealth(10);
    	k.setMilitary(5);
    	k.setPiety(5);
    	k.setPopularity(5);
    	k.setWealth(5);
    	game.setKingdom(k);
    }

    /**
     *  Get all the games.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GameDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        return gameRepository.findAll(pageable)
            .map(gameMapper::toDto);
    }

    /**
     *  Get one game by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public GameDTO findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        Game game = gameRepository.findOne(id);
        return gameMapper.toDto(game);
    }

    /**
     *  Delete the  game by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.delete(id);
    }
    
    public void process(Long id) {
    }
}
