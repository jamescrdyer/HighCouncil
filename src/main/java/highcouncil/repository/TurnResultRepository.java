package highcouncil.repository;

import highcouncil.domain.TurnResult;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TurnResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurnResultRepository extends JpaRepository<TurnResult, Long> {

}
