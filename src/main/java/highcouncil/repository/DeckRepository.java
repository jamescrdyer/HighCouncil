package highcouncil.repository;

import highcouncil.domain.Deck;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Deck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    @Query("select distinct deck from Deck deck left join fetch deck.cards left join fetch deck.discards")
    List<Deck> findAllWithEagerRelationships();

    @Query("select deck from Deck deck left join fetch deck.cards left join fetch deck.discards where deck.id =:id")
    Deck findOneWithEagerRelationships(@Param("id") Long id);

}
