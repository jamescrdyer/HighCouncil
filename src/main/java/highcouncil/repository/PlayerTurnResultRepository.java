package highcouncil.repository;

import highcouncil.domain.PlayerTurnResult;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PlayerTurnResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerTurnResultRepository extends JpaRepository<PlayerTurnResult, Long> {

}
