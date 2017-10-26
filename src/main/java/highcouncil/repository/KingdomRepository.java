package highcouncil.repository;

import highcouncil.domain.Kingdom;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Kingdom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Long> {

}
