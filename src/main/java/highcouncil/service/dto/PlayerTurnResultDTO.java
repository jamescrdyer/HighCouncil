package highcouncil.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import highcouncil.domain.enumeration.Action;

/**
 * A DTO for the PlayerTurnResult entity.
 */
public class PlayerTurnResultDTO implements Serializable {

    private Long id;

    private Integer turn;

    private Action action;

    private Long gameId;

    private Long playerId;

    private String playerLogin;

    private String displayName;

    private String summary;

    private Long turnResultId;

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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getTurnResultId() {
        return turnResultId;
    }

    public void setTurnResultId(Long turnResultId) {
        this.turnResultId = turnResultId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerTurnResultDTO playerTurnResultDTO = (PlayerTurnResultDTO) o;
        if(playerTurnResultDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerTurnResultDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlayerTurnResultDTO{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", turn='" + getTurn() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }
}
