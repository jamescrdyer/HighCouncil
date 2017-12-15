package highcouncil.repository;

import highcouncil.domain.ActionResolution;
import highcouncil.domain.Game;
import highcouncil.domain.enumeration.Action;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ActionResolution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionResolutionRepository extends JpaRepository<ActionResolution, Long> {
    @Query("select ar from ActionResolution ar where ar.orderResolution.minimum >= :orderAmount and ar.orderResolution.maximum <= :orderAmount and ar.action = :action")
	ActionResolution findOneByTotalAndAction(@Param("orderAmount") int orderAmount, @Param("action") Action action);
}
