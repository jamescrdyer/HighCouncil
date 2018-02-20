package highcouncil.service;

import highcouncil.domain.ActionResolution;
import highcouncil.domain.ExpectedOrderNumber;
import highcouncil.domain.Game;
import highcouncil.domain.Kingdom;
import highcouncil.domain.Orders;
import highcouncil.domain.Player;
import highcouncil.domain.PlayerTurnResult;
import highcouncil.domain.StatHolder;
import highcouncil.domain.TurnResult;
import highcouncil.domain.User;
import highcouncil.domain.enumeration.Action;
import highcouncil.domain.enumeration.Phase;
import highcouncil.repository.ActionResolutionRepository;
import highcouncil.repository.ExpectedOrderNumberRepository;
import highcouncil.repository.GameRepository;
import highcouncil.repository.OrdersRepository;
import highcouncil.repository.PlayerRepository;
import highcouncil.repository.TurnResultRepository;
import highcouncil.repository.UserRepository;
import highcouncil.service.dto.GameDTO;
import highcouncil.service.mapper.GameMapper;
import highcouncil.service.mapper.TurnResultMapper;
import highcouncil.web.websocket.dto.DiscussionDTO;

import java.security.Principal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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

	private final UserService userService;

	private final GameRepository gameRepository;

	private final TurnResultRepository turnResultRepository;

	private final PlayerRepository playerRepository;

	private final OrdersRepository ordersRepository;

	private final ExpectedOrderNumberRepository expectedOrdersRepository;

	private final ActionResolutionRepository actionResolutionRepository;

	private final GameMapper gameMapper;

	private final TurnResultMapper turnResultMapper;

	private final CodeResolver codeResolver;

	private final SimpMessagingTemplate messagingTemplate;

	public GameService(GameRepository gameRepo, OrdersRepository ordersRepo, PlayerRepository playerRepo,
			UserService userService, ActionResolutionRepository actionResolutionRepo, ExpectedOrderNumberRepository expectedOrdersRepo, 
			TurnResultRepository turnResultRepo,
			GameMapper gameMapper, TurnResultMapper turnResultMapper, CodeResolver codeResolver, SimpMessagingTemplate messagingTemplate) 
	{
		this.gameRepository = gameRepo;
		this.userService = userService;
		this.ordersRepository = ordersRepo;
		this.expectedOrdersRepository = expectedOrdersRepo;
		this.actionResolutionRepository = actionResolutionRepo;
		this.gameMapper = gameMapper;
		this.codeResolver = codeResolver;
		this.messagingTemplate = messagingTemplate;
		this.playerRepository = playerRepo;
		this.turnResultRepository = turnResultRepo;
		this.turnResultMapper = turnResultMapper;
	}

	/**
	 * Save a game.
	 *
	 * @param gameDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public GameDTO save(GameDTO gameDTO) {
		log.debug("Request to save Game : {}", gameDTO);
		Game game = gameMapper.toEntity(gameDTO);
		if (game.getId() == null) {
			initKingdom(game);
		}
		game = gameRepository.save(game);
		return gameMapper.toDto(game);
	}

	private void initKingdom(Game game) {
		Kingdom k = new Kingdom();
		k.setHealth(10);
		k.setMilitary(4);
		k.setPiety(4);
		k.setPopularity(4);
		k.setWealth(4);
		k.setGame(game);
		game.setKingdom(k);
	}

	/**
	 * Get all the games.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<GameDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Games");
		return gameRepository.findAll(pageable).map(gameMapper::toDto);
	}

	/**
	 * Get all the games.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<GameDTO> findForming(Pageable pageable) {
		log.debug("Request to get forming Games");
		return gameRepository.findAllForming(pageable).map(gameMapper::toDto);
	}

	/**
	 * Get one game by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public GameDTO findOne(Long id) {
		log.debug("Request to get Game : {}", id);
		Game game = gameRepository.findOne(id);
		return gameMapper.toDto(game);
	}

	/**
	 * Delete the game by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Game : {}", id);
		gameRepository.delete(id);
	}

	@Scheduled(cron = "${cron.gameProcessor}")
	public void processGames() {
		List<Game> games = gameRepository.findAllNotComplete();
		if (games != null) {
			games.forEach(g -> {
				processGame(g);
			});
		}
		games = gameRepository.findAllForming();
		if (games != null) {
			games.forEach(g -> {
				startGame(g);
			});
		}
	}

	private Game startGame(Game game) {
		if (game.getPlayers().size() == 3 || game.getPlayers().size() == 4) {
			game.setPhase(Phase.Orders);
			game.setTurn(1);
			game.getPlayers().stream().findFirst().get().setChancellor(true);
			setExpectedOrders(game);
			Game result = gameRepository.save(game);
			afterGameProcessed(result, null);
			return result;
		}
		return game;
	}

	private void processGame(Game game) {
		List<Player> players = game.getPlayersList();
		for (Player p : players) {
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
			TurnResult turnResult = new TurnResult();
			turnResult.setTurn(game.getTurn());
			turnResult.setGame(game);
			turnResult.setPiety(piety);
			turnResult.setPopularity(popularity);
			turnResult.setMilitary(military);
			turnResult.setWealth(wealth);
			turnResult.setFavour(favour);
			PlayerTurnResult kingdomResult = new PlayerTurnResult();
			kingdomResult.setGame(game);
			kingdomResult.setTurn(game.getTurn());
			turnResult.addPlayerTurnResult(kingdomResult);
			
			for (Player p : players) {
				boolean isChancellor = p.isChancellor();
				
				Optional<Orders> ordersFound = allOrders.stream().filter(o -> o.getPlayer().equals(p)).findFirst();
				if (ordersFound.isPresent()) {
					Action action = ordersFound.get().getAction();
					int total = 0;
					switch (action) {
					case Favour:
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
					PlayerTurnResult playerResult = applyPlayerResolution(p, isChancellor, resolution);
					playerResult.setAction(action);
					playerResult.setGame(game);
					playerResult.setTurn(game.getTurn());
					turnResult.addPlayerTurnResult(playerResult);
					if (isChancellor) { // chancellor penalties
						if (wealth <= 0) {
							p.modifyWealth(-1);
							kingdom.modifyWealth(-1);
							playerResult.modifyWealth(-1);
							kingdomResult.modifyWealth(-1);
						}
						if (military <= 0) {
							p.modifyMilitary(-1);
							kingdom.modifyMilitary(-1);
							playerResult.modifyMilitary(-1);
							kingdomResult.modifyMilitary(-1);
						}
						if (piety <= 0) {
							p.modifyPiety(-1);
							kingdom.modifyPiety(-1);
							playerResult.modifyPiety(-1);
							kingdomResult.modifyPiety(-1);
						}
						if (popularity <= 0) {
							p.modifyPopularity(-1);
							kingdom.modifyPopularity(-1);
							playerResult.modifyPopularity(-1);
							kingdomResult.modifyPopularity(-1);
						}
						if (favour <= 0) {
							p.modifyFavour(-1);
							playerResult.modifyFavour(-1);
						}
					}
					if (!kingdomActionsApplied.contains(action)) {
						applyKingdomResolution(kingdom, resolution.getCodeKingdom(), kingdomResult);
					}
					addPenalties(p);
				}
				p.setPhaseLocked(false);
			}
			game.setTurn(game.getTurn() + 1);
			setNextChancellor(game);
			kingdom.setHealth(kingdom.getHealth() - 1);
			checkGameEndAndScore(game);
			Game result = gameRepository.save(game);
			turnResultRepository.save(turnResult);
			afterGameProcessed(result, turnResult);
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

		for (Player p : game.getPlayers()) {
			int score = 0;
			score += p.getWealth();
			score += p.getMilitary() * 2;
			score += p.getPopularity() / 2 + p.getPopularity() % 2;
			score -= p.getPenalty() * 3;
			if (kingdom.getHealth() <= 0) {
				score += p.getFavour();
				gameOver = true;
			}
			p.setScore(score);

			// calculate maxes
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
		for (Player p : game.getPlayers()) {
			int score = p.getScore();

			if (playerMaxes.getPiety().equals(p.getPiety())) {
				int bonus = 8;
				if (kingdom.getPiety() <= 0) {
					gameOver = true;
					bonus += KINGDOM_0_BONUS;
				}
				bonus = bonus / playerCounts.getPiety();
				score += bonus;
			}
			if (pietyMin == p.getPiety()) {
				int penalty = 8 / pietyMinCount;
				score -= penalty;
			}
			if (playerMaxes.getPopularity().equals(p.getPopularity())) {
				int bonus = 5;
				if (kingdom.getPopularity() <= 0) {
					gameOver = true;
					bonus += KINGDOM_0_BONUS;
				}
				bonus = bonus / playerCounts.getPopularity();
				score += bonus;
			}
			if (popularityMin == p.getPopularity()) {
				int penalty = 3 / popularityMinCount;
				score -= penalty;
			}
			if (kingdom.getMilitary() <= 0) {
				gameOver = true;
				if (playerMaxes.getMilitary().equals(p.getMilitary())) {
					int bonus = KINGDOM_0_BONUS;
					bonus = bonus / playerCounts.getMilitary();
					score += bonus;
				}
			}
			if (kingdom.getWealth() <= 0) {
				gameOver = true;
				if (playerMaxes.getWealth().equals(p.getWealth())) {
					int bonus = KINGDOM_0_BONUS;
					bonus = bonus / playerCounts.getWealth();
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
		boolean lastWasChancellor = false;
		for (Player p : players) {
			if (p.isChancellor()) {
				lastWasChancellor = true;
				p.setChancellor(false);
			}
			if (lastWasChancellor) {
				p.setChancellor(true);
				lastWasChancellor = false;
				break;
			}
		}
		if (lastWasChancellor) {
			players.get(0).setChancellor(true);
		}
		setExpectedOrders(game);
	}
	
	private void setExpectedOrders(Game game) {
		List<Player> players = game.getPlayersList();
		List<ExpectedOrderNumber> expectedOrders = expectedOrdersRepository.findByNumberOfPlayers(players.size());
		expectedOrders.sort((o1,o2) -> o1.getPlayerNumber().compareTo(o2.getPlayerNumber()));
		for (int i = 0; i<players.size(); i++) {
			Player chancellor = players.get(i);
			if (chancellor.isChancellor()) {
				int chancellorIndex = i;
				for (int j = 0; j<expectedOrders.size(); j++) {
					ExpectedOrderNumber ordersExpected = expectedOrders.get(j);
					players.get((chancellorIndex+j) % players.size()).setOrdersExpected(ordersExpected.getOrdersExpected());
				}
				break;
			}
		}
		playerRepository.save(players);
	}

	public void afterGameProcessed(Game game, TurnResult result) {
		GameDTO gameDto = gameMapper.toDto(game);
		gameDto.setTurnResult(turnResultMapper.turnResultToTurnResultDTO(result));
		this.messagingTemplate.convertAndSend("/topic/gamestate/" + game.getId(), gameDto);
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

	private PlayerTurnResult applyPlayerResolution(Player p, boolean isChancellor, ActionResolution resolution) {
		PlayerTurnResult playerResult = new PlayerTurnResult();
		playerResult.setPlayer(p);
		codeResolver.resolveCode(resolution.getCodeNormal(), p, playerResult);
		if (isChancellor) {
			codeResolver.resolveCode(resolution.getCodeChancellor(), p, playerResult);
		}
		return playerResult;
	}

	private void applyKingdomResolution(StatHolder target, String code, PlayerTurnResult kingdomResult) {
		codeResolver.resolveCode(code, target, kingdomResult);
	}

	public GameDTO join(Long id) {
		Game game = gameRepository.findOne(id);
		if (game == null || game.getPhase() != Phase.Forming) {
			return null;
		}
		Player p = new Player();
		User u = userService.getUserWithAuthorities();
		p.setUser(u);
		p.setName(u.getLogin());
		game.getPlayers().add(p);
		p.setGame(game);
		initPlayer(p);
		playerRepository.save(p);
		Game result = gameRepository.save(game);
		result = startGame(result);
		return gameMapper.toDto(game);
	}

	private void initPlayer(Player p) {
		p.setPiety(2);
		p.setWealth(3);
		p.setPopularity(2);
		p.setMilitary(2);
		p.setFavour(3);
		p.setPenalty(0);
		p.setChancellor(false);
		p.setScore(0);
		p.setPhaseLocked(false);
	}
}
