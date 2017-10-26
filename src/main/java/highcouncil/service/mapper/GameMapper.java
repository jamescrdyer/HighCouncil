package highcouncil.service.mapper;

import highcouncil.domain.*;
import highcouncil.service.dto.GameDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Game and its DTO GameDTO.
 */
@Mapper(componentModel = "spring", uses = {KingdomMapper.class, DeckMapper.class, PlayerMapper.class})
public interface GameMapper extends EntityMapper<GameDTO, Game> {

    @Mapping(source = "deck.id", target = "deckId")
    GameDTO toDto(Game game); 

    @Mapping(source = "deckId", target = "deck")
    Game toEntity(GameDTO gameDTO);

    default Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
