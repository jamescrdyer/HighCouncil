package highcouncil.repository;

import highcouncil.domain.ActionResolution;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ActionResolution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionResolutionRepository extends JpaRepository<ActionResolution, Long> {

}
