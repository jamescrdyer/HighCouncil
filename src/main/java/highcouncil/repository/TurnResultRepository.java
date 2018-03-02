package highcouncil.repository;

import highcouncil.domain.TurnResult;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TurnResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurnResultRepository extends JpaRepository<TurnResult, Long> {

    @Query("select tr from TurnResult tr left join fetch tr.playerTurnResults where tr.game.id = :gameId and tr.turn = :turn")
	TurnResult findOneByGameIdAndTurn(@Param("gameId") Long gameId, @Param("turn") int turn);
}
