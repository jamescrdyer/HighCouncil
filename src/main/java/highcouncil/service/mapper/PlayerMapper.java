package highcouncil.service.mapper;

import highcouncil.domain.*;
import highcouncil.service.dto.PlayerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Player and its DTO PlayerDTO.
 */
@Mapper(componentModel = "spring", uses = {GameMapper.class, UserMapper.class, CardMapper.class})
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {

    @Mapping(source = "game.id", target = "gameId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "user.displayName", target = "displayName")
    PlayerDTO toDto(Player player); 

    @Mapping(source = "gameId", target = "game")
    @Mapping(source = "userId", target = "user")
    Player toEntity(PlayerDTO playerDTO);

    default Player fromId(Long id) {
        if (id == null) {
            return null;
        }
        Player player = new Player();
        player.setId(id);
        return player;
    }
}
