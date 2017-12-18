package highcouncil.service;

import highcouncil.domain.ActionResolution;
import highcouncil.domain.Game;
import highcouncil.domain.Orders;
import highcouncil.domain.Player;
import highcouncil.domain.StatHolder;
import highcouncil.domain.enumeration.Action;
import highcouncil.repository.ActionResolutionRepository;
import highcouncil.repository.GameRepository;
import highcouncil.repository.OrdersRepository;
import highcouncil.service.dto.GameDTO;
import highcouncil.service.mapper.GameMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final OrdersRepository ordersRepository;

    private final ActionResolutionRepository actionResolutionRepository;

    private final GameMapper gameMapper;

    private final CodeResolver codeResolver;
    
    public GameService(GameRepository gameRepository, OrdersRepository ordersRepository, ActionResolutionRepository actionResolutionRepository, 
    		GameMapper gameMapper, CodeResolver codeResolver) {
        this.gameRepository = gameRepository;
        this.ordersRepository = ordersRepository;
        this.actionResolutionRepository = actionResolutionRepository;
        this.gameMapper = gameMapper;
        this.codeResolver = codeResolver;
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
        return gameMapper.toDto(game);
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
     *  Delete the game by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.delete(id);
    }
    
	@Scheduled(cron = "${cron.gameProcessor}")
    public void processGames() {
        List<Game> games = gameRepository.findAllNotComplete();
        games.forEach(g -> {
        	processGame(g);
        });
    }
	

	private void processGame(Game game) {
		for (Player p: game.getPlayers()) {
			if (!p.isPhaseLocked()) {
				return;
			}
		}
		List<Orders> allOrders = ordersRepository.findByGameAndTurn(game.getId(), game.getTurn());
		if (allOrders.size() == game.getPlayers().size()) {
			Set<Action> kingdomActionsApplied = new HashSet<Action>();
			int wealth = allOrders.parallelStream().mapToInt(o -> o.getWealth()).sum();
			int military = allOrders.parallelStream().mapToInt(o -> o.getMilitary()).sum();
			int piety = allOrders.parallelStream().mapToInt(o -> o.getPiety()).sum();
			int popularity = allOrders.parallelStream().mapToInt(o -> o.getPopularity()).sum();
			int favour = allOrders.parallelStream().mapToInt(o -> o.getFavour()).sum();
			for (Player p: game.getPlayers()) {
				boolean isChancellor = p.getId().equals(game.getFirstPlayerId());
				Optional<Orders> ordersFound = allOrders.stream().filter(o -> o.getPlayer().equals(p)).findFirst();
				if (ordersFound.isPresent()) {
					Action action = ordersFound.get().getAction();
					int total = 0;
					switch (action) {
					case Indulge:
						total = favour;
						break;
					case Military:
						total = military;
						break;
					case Piety:
						total = piety;
						break;
					case Popularity:
						total = popularity;
						break;
					case Wealth:
						total = wealth;
						break;
					default:
						break;
					}
					ActionResolution resolution = actionResolutionRepository.findOneByTotalAndAction(total, action);
					applyResolution(p, isChancellor, resolution);
					if (isChancellor) { //chancellor penalties
						if (wealth <= 0) {
							p.setWealth(p.getWealth() - 1);
						}
						if (military <= 0) {
							p.setMilitary(p.getMilitary() - 1);
						}
						if (piety <= 0) {
							p.setPiety(p.getPiety() - 1);
						}
						if (popularity <= 0) {
							p.setPopularity(p.getPopularity() - 1);
						}
						if (favour <= 0) {
							p.setFavour(p.getFavour() - 1);
						}
					}
					if (!kingdomActionsApplied.contains(action)) {
						applyResolution(game.getKingdom(), resolution.getCodeKingdom());
					}
					addPenalties(p);
				}
			}
		}
	}

	private void addPenalties(Player p) {
		while (p.getWealth() < 0) {
			p.setWealth(p.getWealth() + 1);
			p.setPenalty(p.getPenalty() + 1);
		}
		while (p.getMilitary() < 0) {
			p.setMilitary(p.getMilitary() + 1);
			p.setPenalty(p.getPenalty() + 1);
		}
		while (p.getPiety() < 0) {
			p.setPiety(p.getPiety() + 1);
			p.setPenalty(p.getPenalty() + 1);
		}
		while (p.getPopularity() < 0) {
			p.setPopularity(p.getPopularity() + 1);
			p.setPenalty(p.getPenalty() + 1);
		}
		while (p.getFavour() < 0) {
			p.setFavour(p.getFavour() + 1);
			p.setPenalty(p.getPenalty() + 1);
		}
	}
	private void applyResolution(Player p, boolean isChancellor, ActionResolution resolution) {
		applyResolution(p, resolution.getCodeNormal());
		if (isChancellor) {
			applyResolution(p, resolution.getCodeChancellor());
		}
	}

	private void applyResolution(StatHolder target, String code) {
		codeResolver.resolveCode(code, target);
	}
}
