package highcouncil.repository;

import highcouncil.domain.Deck;
import highcouncil.domain.Orders;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Orders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("select orders from Orders orders where orders.game.id =:gameId and orders.player.id =:playerId and orders.turn=:turn")
	Orders findOneByGamePlayerAndTurn(@Param("gameId") Long gameId, @Param("playerId") Long playerId, @Param("turn") Integer turn); 

    @Query("select orders from Orders orders where orders.game.id =:gameId and orders.turn=:turn and orders.locked = true")
	List<Orders> findByLockedByGameAndTurn(@Param("gameId") Long gameId, @Param("turn") Integer turn); 
}
