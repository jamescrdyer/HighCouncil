package highcouncil.service.mapper;

import highcouncil.domain.*;
import highcouncil.service.dto.DeckDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Deck and its DTO DeckDTO.
 */
@Mapper(componentModel = "spring", uses = {CardMapper.class})
public interface DeckMapper extends EntityMapper<DeckDTO, Deck> {

    

    @Mapping(target = "game", ignore = true)
    Deck toEntity(DeckDTO deckDTO);

    default Deck fromId(Long id) {
        if (id == null) {
            return null;
        }
        Deck deck = new Deck();
        deck.setId(id);
        return deck;
    }
}
