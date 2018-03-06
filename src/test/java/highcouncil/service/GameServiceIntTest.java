package highcouncil.service;

import highcouncil.HighCouncilApp;
import highcouncil.config.Constants;
import highcouncil.domain.Game;
import highcouncil.domain.Kingdom;
import highcouncil.domain.Player;
import highcouncil.domain.enumeration.Phase;
import highcouncil.repository.GameRepository;
import highcouncil.service.dto.GameDTO;
import highcouncil.service.util.RandomUtil;
import highcouncil.web.rest.KingdomResourceIntTest;
import highcouncil.web.rest.PlayerResourceIntTest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the GameResource REST controller.
 *
 * @see GameService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HighCouncilApp.class)
@Transactional
public class GameServiceIntTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    private Game game;
    private Player p1;
    private Player p2;
    private Player p3;
    private Kingdom kingdom;

    @Autowired
    private EntityManager em;

    @Before
    public void init() {
        game = new Game();
        game.setPhase(Phase.Orders);
    	
        p1 = PlayerResourceIntTest.createEntity(em);
    	p1.setGame(game);
    	game.getPlayers().add(p1);
    	p2 = PlayerResourceIntTest.createEntity(em);
    	p2.setGame(game);
    	game.getPlayers().add(p2);
    	p3 = PlayerResourceIntTest.createEntity(em);
    	p3.setGame(game);
    	game.getPlayers().add(p3);
    	kingdom = KingdomResourceIntTest.createEntity(em);
    	game.setKingdom(kingdom);
    	kingdom.setGame(game);
    }

    @Test
    @Transactional
    public void checkScoresEven() {
    	gameService.checkGameEndAndScore(game);
    	int score = p1.getScore();
        assertThat(p2.getScore()).isEqualTo(score);
        assertThat(p3.getScore()).isEqualTo(score);
    }

    @Test
    @Transactional
    public void checkScoresPiety() {
    	p2.modifyPiety(1);
    	p3.modifyPiety(-1);
    	gameService.checkGameEndAndScore(game);
    	int score = p1.getScore();
        assertThat(p2.getScore()).isEqualTo(score+GameService.MOST_PIETY_BONUS);
        assertThat(p3.getScore()).isEqualTo(score-GameService.LEAST_PIETY_PENALTY);
    }

    @Test
    @Transactional
    public void checkScoresPietySharedPenalty() {
    	p1.modifyPiety(1);
    	gameService.checkGameEndAndScore(game);
    	int score = p1.getScore()-GameService.LEAST_PIETY_PENALTY/2-GameService.MOST_PIETY_BONUS;
        assertThat(p2.getScore()).isEqualTo(score);
        assertThat(p3.getScore()).isEqualTo(score);
    }

    @Test
    @Transactional
    public void checkScoresPopularitySharedPenalty() {
    	p1.setPopularity(1);
    	p2.setPopularity(0);
    	p3.setPopularity(0);
    	gameService.checkGameEndAndScore(game);
    	int score = p1.getScore()-GameService.LEAST_POPULARITY_PENALTY/2-GameService.MOST_POPULARITY_BONUS;
        assertThat(p2.getScore()).isEqualTo(score);
        assertThat(p3.getScore()).isEqualTo(score);
    }

    @Test
    @Transactional
    public void checkScoresFavour() {
    	p1.setFavour(1);
    	kingdom.setHealth(5);
    	gameService.checkGameEndAndScore(game);
    	int score = p1.getScore();
    	kingdom.setHealth(0);
    	gameService.checkGameEndAndScore(game);
        assertThat(p1.getScore()).isEqualTo(score+1);
        assertThat(game.getPhase()).isEqualTo(Phase.Completed);
    }

    @Test
    @Transactional
    public void checkScoresExample() {
    	p1.setPiety(13);
    	p1.setPopularity(3);
    	p1.setMilitary(1);
    	p1.setWealth(6);
    	p1.setFavour(10);
    	
    	p2.setPiety(10);
    	p2.setPopularity(8);
    	p2.setMilitary(4);
    	p2.setWealth(2);
    	p2.setFavour(6);
    	
    	p3.setPiety(11);
    	p3.setPopularity(6);
    	p3.setMilitary(7);
    	p3.setWealth(0);
    	p3.setFavour(10);
    	p3.setPenalty(2);
    	
    	kingdom.setHealth(0);
    	kingdom.setWealth(-1);

    	gameService.checkGameEndAndScore(game);
        assertThat(p1.getScore()).isEqualTo(30);
        assertThat(p2.getScore()).isEqualTo(17);
        assertThat(p3.getScore()).isEqualTo(24);
    }
}
