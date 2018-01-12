package highcouncil.repository;

import highcouncil.domain.Game;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("select game from Game game where game.phase != 'Completed'")
	List<Game> findAllNotComplete();

    @Query("select game from Game game where game.phase = 'Forming'")
	Page<Game> findAllForming(Pageable pageable);

    @Query("select game from Game game where game.phase = 'Forming'")
	List<Game> findAllForming();
}
