package highcouncil.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TurnResult entity.
 */
public class TurnResultDTO implements Serializable {
	private static final long serialVersionUID = 7326342189871432958L;

	private Long id;

	private String summary;
	
    private Integer turn;

    private Long gameId;

    private Set<PlayerTurnResultDTO> playerTurnResults = new HashSet<PlayerTurnResultDTO>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Set<PlayerTurnResultDTO> getPlayerTurnResults() {
		return playerTurnResults;
	}

	public void setPlayerTurnResults(Set<PlayerTurnResultDTO> playerTurnResults) {
		this.playerTurnResults = playerTurnResults;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurnResultDTO turnResultDTO = (TurnResultDTO) o;
        if(turnResultDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turnResultDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurnResultDTO{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", turn='" + getTurn() + "'" +
            "}";
    }
}
