package highcouncil.service;

import highcouncil.domain.ActionResolution;
import highcouncil.domain.Game;
import highcouncil.domain.Kingdom;
import highcouncil.domain.Orders;
import highcouncil.domain.Player;
import highcouncil.domain.StatHolder;
import highcouncil.domain.enumeration.Action;
import highcouncil.domain.enumeration.Phase;
import highcouncil.repository.ActionResolutionRepository;
import highcouncil.repository.GameRepository;
import highcouncil.repository.OrdersRepository;
import highcouncil.service.dto.GameDTO;
import highcouncil.service.mapper.GameMapper;
import highcouncil.web.websocket.dto.DiscussionDTO;

import java.security.Principal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameService {
	private static final int KINGDOM_0_BONUS = 6;

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final OrdersRepository ordersRepository;

    private final ActionResolutionRepository actionResolutionRepository;

    private final GameMapper gameMapper;

    private final CodeResolver codeResolver;

    private final SimpMessagingTemplate messagingTemplate;
    
    public GameService(GameRepository gameRepository, OrdersRepository ordersRepository, ActionResolutionRepository actionResolutionRepository, 
    		GameMapper gameMapper, CodeResolver codeResolver, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;
        this.ordersRepository = ordersRepository;
        this.actionResolutionRepository = actionResolutionRepository;
        this.gameMapper = gameMapper;
        this.codeResolver = codeResolver;
        this.messagingTemplate = messagingTemplate;
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
        games = gameRepository.findAllForming();
        games.forEach(g -> {
        	startGame(g);
        });
    }
	
	private void startGame(Game game) {
		if (game.getPlayers().size() == 3 || game.getPlayers().size() == 4) {
			game.setPhase(Phase.Orders);
			game.setFirstPlayerId(game.getPlayers().stream().findFirst().get().getId());
		}
	}

	private void processGame(Game game) {
		List<Player> players = game.getPlayersList();
		for (Player p: players) {
			if (!p.isPhaseLocked()) {
				return;
			}
		}
		List<Orders> allOrders = ordersRepository.findByGameAndTurn(game.getId(), game.getTurn());
		if (allOrders.size() == players.size()) {
			Set<Action> kingdomActionsApplied = new HashSet<Action>();
			Kingdom kingdom = game.getKingdom();
			int wealth = allOrders.parallelStream().mapToInt(o -> o.getWealth()).sum();
			int military = allOrders.parallelStream().mapToInt(o -> o.getMilitary()).sum();
			int piety = allOrders.parallelStream().mapToInt(o -> o.getPiety()).sum();
			int popularity = allOrders.parallelStream().mapToInt(o -> o.getPopularity()).sum();
			int favour = allOrders.parallelStream().mapToInt(o -> o.getFavour()).sum();
			for (Player p: players) {
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
							kingdom.setWealth(kingdom.getWealth() - 1);
						}
						if (military <= 0) {
							p.setMilitary(p.getMilitary() - 1);
							kingdom.setMilitary(kingdom.getMilitary() - 1);
						}
						if (piety <= 0) {
							p.setPiety(p.getPiety() - 1);
							kingdom.setPiety(kingdom.getPiety() - 1);
						}
						if (popularity <= 0) {
							p.setPopularity(p.getPopularity() - 1);
							kingdom.setPopularity(kingdom.getPopularity() - 1);
						}
						if (favour <= 0) {
							p.setFavour(p.getFavour() - 1);
						}
					}
					if (!kingdomActionsApplied.contains(action)) {
						applyResolution(kingdom, resolution.getCodeKingdom());
					}
					addPenalties(p);
				}
				p.setPhaseLocked(false);
			}
			game.setTurn(game.getTurn()+1);
			setNextChancellor(game);
			kingdom.setHealth(kingdom.getHealth() - 1);
			checkGameEndAndScore(game);
			gameRepository.save(game);
			afterGameProcessed(game);
		}
	}

	private void checkGameEndAndScore(Game game) {
		Kingdom kingdom = game.getKingdom();
		
		Player playerMaxes = new Player();
		Player playerCounts = new Player();
		playerMaxes.setValuesToZero();
		playerCounts.setValuesToZero();
		int secondMostMilitary = 0;
		
		int pietyMin = Integer.MAX_VALUE;
		int pietyMinCount = 1;
		int popularityMin = Integer.MAX_VALUE;
		int popularityMinCount = 1;
		
		boolean gameOver = false;
		
		for (Player p: game.getPlayers()) {
			int score = 0;
			score += p.getWealth();
			score += p.getMilitary() * 2;
			score += p.getPopularity()/2 + p.getPopularity()%2;
			score -= p.getPenalty() * 3;
			if (kingdom.getHealth() <= 0) {
				score += p.getFavour();
				gameOver = true;
			}
			p.setScore(score);
			
			//calculate maxes
			if (playerMaxes.getPiety() < p.getPiety()) {
				playerMaxes.setPiety(p.getPiety());
				playerCounts.setPiety(1);
			} else if (playerMaxes.getPiety().equals(p.getPiety())) {
				playerCounts.setPiety(playerCounts.getPiety() + 1);
			}
			if (playerMaxes.getPopularity() < p.getPopularity()) {
				playerMaxes.setPopularity(p.getPopularity());
				playerCounts.setPopularity(1);
			} else if (playerMaxes.getPopularity().equals(p.getPopularity())) {
				playerCounts.setPopularity(playerCounts.getPopularity() + 1);
			}

			if (pietyMin > p.getPiety()) {
				pietyMin = p.getPiety();
				pietyMinCount = 1;
			} else if (pietyMin == p.getPiety()) {
				pietyMinCount++;
			}
			if (popularityMin > p.getPopularity()) {
				popularityMin = p.getPopularity();
				popularityMinCount = 1;
			} else if (popularityMin == p.getPopularity()) {
				popularityMinCount++;
			}
			
			if (playerMaxes.getMilitary() < p.getMilitary()) {
				playerMaxes.setMilitary(p.getMilitary());
				playerCounts.setMilitary(1);
			} else {
				if (secondMostMilitary < p.getMilitary()) {
					secondMostMilitary = p.getMilitary();
				}
				if (playerMaxes.getMilitary().equals(p.getMilitary())) {
					playerCounts.setMilitary(playerCounts.getMilitary() + 1);
				}
			}
			if (playerMaxes.getWealth() < p.getWealth()) {
				playerMaxes.setWealth(p.getWealth());
				playerCounts.setWealth(1);
			} else if (playerMaxes.getWealth().equals(p.getWealth())) {
				playerCounts.setWealth(playerCounts.getWealth() + 1);
			}
		}
		for (Player p: game.getPlayers()) {
			int score = p.getScore();
			
			if (playerMaxes.getPiety().equals(p.getPiety())) {
				int bonus = 8;
				if (kingdom.getPiety() <= 0) {
					gameOver = true;
					bonus += KINGDOM_0_BONUS;
				}
				bonus = bonus/playerCounts.getPiety();
				score += bonus;
			}
			if (pietyMin == p.getPiety()) {
				int penalty = 8/pietyMinCount;
				score -= penalty;
			}
			if (playerMaxes.getPopularity().equals(p.getPopularity())) {
				int bonus = 5;
				if (kingdom.getPopularity() <= 0) {
					gameOver = true;
					bonus += KINGDOM_0_BONUS;
				}
				bonus = bonus/playerCounts.getPopularity();
				score += bonus;
			}
			if (popularityMin == p.getPopularity()) {
				int penalty = 3/popularityMinCount;
				score -= penalty;
			}
			if (kingdom.getMilitary() <= 0) {
				gameOver = true;
				if (playerMaxes.getMilitary().equals(p.getMilitary())) {
					int bonus = KINGDOM_0_BONUS;
					bonus = bonus/playerCounts.getMilitary();
					score += bonus;
				}
			}
			if (kingdom.getWealth() <= 0) {
				gameOver = true;
				if (playerMaxes.getWealth().equals(p.getWealth())) {
					int bonus = KINGDOM_0_BONUS;
					bonus = bonus/playerCounts.getWealth();
					score += bonus;
				}
			}
			p.setScore(score);
		}
		if (gameOver) {
			game.setPhase(Phase.Completed);
		}
	}
	
	private void setNextChancellor(Game game) {
		List<Player> players = game.getPlayersList();
		Player chancellorCompare = new Player();
		chancellorCompare.setId(game.getFirstPlayerId());
		int chancellorIndex = (players.indexOf(chancellorCompare) + 1) % players.size();
		Player nextChancellor = players.get(chancellorIndex);
		game.setFirstPlayerId(nextChancellor.getId());
	}
	public void afterGameProcessed(Game game) {
		this.messagingTemplate.convertAndSend("/topic/gamestate/"+game.getId(), gameMapper.toDto(game));
	}
    
	@MessageMapping("/topic/gamestate/{gameId}")
    public GameDTO gameState(StompHeaderAccessor stompHeaderAccessor, Principal principal) {
		return null;
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
