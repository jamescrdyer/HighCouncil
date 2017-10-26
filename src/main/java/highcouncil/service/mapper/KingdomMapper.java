package highcouncil.service.mapper;

import highcouncil.domain.*;
import highcouncil.service.dto.KingdomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Kingdom and its DTO KingdomDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KingdomMapper extends EntityMapper<KingdomDTO, Kingdom> {
    @Mapping(target = "game", ignore = true)
    Kingdom toEntity(KingdomDTO kingdomDTO);

    default Kingdom fromId(Long id) {
        if (id == null) {
            return null;
        }
        Kingdom kingdom = new Kingdom();
        kingdom.setId(id);
        return kingdom;
    }
}
