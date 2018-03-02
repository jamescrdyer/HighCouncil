package highcouncil.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import highcouncil.domain.enumeration.Phase;

/**
 * A DTO for the Game entity.
 */
public class GameDTO implements Serializable {
	private static final long serialVersionUID = 1832581910303922802L;

	private Long id;

    private Integer timeLimitSeconds;

    private Phase phase;

    @NotNull
    @Min(value = 1)
    private Integer turn;

    @NotNull
    @Min(value = 0)
    private Integer randomOrderNumber;

    private KingdomDTO kingdom;
    
    private TurnResultDTO turnResult;

    private Long deckId;

    private Set<PlayerDTO> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public void setTimeLimitSeconds(Integer timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

	public Integer getRandomOrderNumber() {
		return randomOrderNumber;
	}

	public void setRandomOrderNumber(Integer randomOrderNumber) {
		this.randomOrderNumber = randomOrderNumber;
	}

	public KingdomDTO getKingdom() {
		return kingdom;
	}

	public void setKingdom(KingdomDTO kingdom) {
		this.kingdom = kingdom;
	}

	public TurnResultDTO getTurnResult() {
		return turnResult;
	}

	public void setTurnResult(TurnResultDTO turnResult) {
		this.turnResult = turnResult;
	}

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public Set<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerDTO> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameDTO gameDTO = (GameDTO) o;
        if(gameDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + getId() +
            ", timeLimitSeconds='" + getTimeLimitSeconds() + "'" +
            ", phase='" + getPhase() + "'" +
            ", turn='" + getTurn() + "'" +
            "}";
    }
}
