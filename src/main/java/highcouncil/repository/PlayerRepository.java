package highcouncil.repository;

import highcouncil.domain.Player;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("select player from Player player where player.user.login = ?#{principal.username}")
    List<Player> findByUserIsCurrentUser();
    @Query("select distinct player from Player player left join fetch player.hands")
    List<Player> findAllWithEagerRelationships();

    @Query("select player from Player player left join fetch player.hands where player.id =:id")
    Player findOneWithEagerRelationships(@Param("id") Long id);

}
