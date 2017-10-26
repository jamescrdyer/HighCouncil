package highcouncil.repository;

import highcouncil.domain.OrderResolution;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OrderResolution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderResolutionRepository extends JpaRepository<OrderResolution, Long> {

}
