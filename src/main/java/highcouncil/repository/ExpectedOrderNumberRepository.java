package highcouncil.repository;

import highcouncil.domain.ExpectedOrderNumber;
import highcouncil.domain.Game;
import highcouncil.domain.Player;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ExpectedOrderNumbers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpectedOrderNumberRepository extends JpaRepository<ExpectedOrderNumber, Long> {

    @Query("select orderNumber from ExpectedOrderNumber orderNumber where orderNumber.numberOfPlayers = :numberOfPlayers")
	List<ExpectedOrderNumber> findByNumberOfPlayers(@Param("numberOfPlayers") Integer numberOfPlayers);
}
