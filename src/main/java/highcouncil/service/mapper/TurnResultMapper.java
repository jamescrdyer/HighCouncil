package highcouncil.service.mapper;

import highcouncil.service.dto.TurnResultDTO;
import highcouncil.domain.PlayerTurnResult;
import highcouncil.domain.StatHolder;
import highcouncil.domain.TurnResult;
import highcouncil.service.dto.PlayerTurnResultDTO;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TurnResultMapper {

    public List<TurnResultDTO> turnResultsToTurnResultDTOs(List<TurnResult> results) {
        return results.stream()
            .filter(Objects::nonNull)
            .map(this::turnResultToTurnResultDTO)
            .collect(Collectors.toList());
    }

    public TurnResultDTO turnResultToTurnResultDTO(TurnResult result) {
    	if (result == null) return null;
        TurnResultDTO dto = new TurnResultDTO();
        dto.setTurn(result.getTurn());
        dto.setId(result.getId());
    	String summary = getStatSummary(result);
    	if (result.getFavour() != 0) {
    		summary += "Favour "+result.getFavour()+" ";
    	}
    	dto.setSummary(summary);
        
        Set<PlayerTurnResultDTO> playerResults = dto.getPlayerTurnResults();
        for (PlayerTurnResult ptr : result.getPlayerTurnResults()) {
        	PlayerTurnResultDTO playerResult = new PlayerTurnResultDTO();
        	playerResult.setAction(ptr.getAction());
        	playerResult.setTurn(ptr.getTurn());
        	if (ptr.getGame() != null) {
            	playerResult.setGameId(ptr.getGame().getId());
        	}
        	if (ptr.getPlayer() != null) {
        		if (ptr.getPlayer().getUser() != null) {
                	playerResult.setPlayerLogin(ptr.getPlayer().getUser().getLogin());
                	playerResult.setDisplayName(ptr.getPlayer().getUser().getDisplayName());
        		}
        		playerResult.setPlayerId(ptr.getPlayer().getId());
        	}
        	summary = getStatSummary(ptr);
        	if (ptr.getFavour() != 0) {
        		summary += "Favour "+ptr.getFavour()+" ";
        	}
        	playerResult.setSummary(summary);
        	playerResults.add(playerResult);
        }
        
        return dto;
    }

    public static String getStatSummary(StatHolder statHolder) {
    	String summary = "";
    	if (statHolder.getPiety() != 0) {
    		summary += "Piety "+statHolder.getPiety()+" ";
    	}
    	if (statHolder.getPopularity() != 0) {
    		summary += "Popularity "+statHolder.getPopularity()+" ";
    	}
    	if (statHolder.getMilitary() != 0) {
    		summary += "Military "+statHolder.getMilitary()+" ";
    	}
    	if (statHolder.getWealth() != 0) {
    		summary += "Wealth "+statHolder.getWealth()+" ";
    	}
    	return summary;
    }
}
