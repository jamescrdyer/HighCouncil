package highcouncil.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TurnResult.
 */
@Entity
@Table(name = "turn_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurnResult extends StatHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "favour")
    private Integer favour;

    @Column(name = "turn")
    private Integer turn;

    @ManyToOne
    private Game game;

    @OneToMany(mappedBy = "turnResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlayerTurnResult> playerTurnResults = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFavour() {
        return favour;
    }

    public TurnResult favour(Integer favour) {
        this.favour = favour;
        return this;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public Integer getTurn() {
        return turn;
    }

    public TurnResult turn(Integer turn) {
        this.turn = turn;
        return this;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Game getGame() {
        return game;
    }

    public TurnResult game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<PlayerTurnResult> getPlayerTurnResults() {
        return playerTurnResults;
    }

    public TurnResult playerTurnResults(Set<PlayerTurnResult> playerTurnResults) {
        this.playerTurnResults = playerTurnResults;
        return this;
    }

    public TurnResult addPlayerTurnResult(PlayerTurnResult playerTurnResult) {
        this.playerTurnResults.add(playerTurnResult);
        playerTurnResult.setTurnResult(this);
        return this;
    }

    public TurnResult removePlayerTurnResult(PlayerTurnResult playerTurnResult) {
        this.playerTurnResults.remove(playerTurnResult);
        playerTurnResult.setTurnResult(null);
        return this;
    }

    public void setPlayerTurnResults(Set<PlayerTurnResult> playerTurnResults) {
        this.playerTurnResults = playerTurnResults;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TurnResult turnResult = (TurnResult) o;
        if (turnResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turnResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurnResult{" +
            "id=" + getId() +
            ", piety='" + getPiety() + "'" +
            ", popularity='" + getPopularity() + "'" +
            ", military='" + getMilitary() + "'" +
            ", wealth='" + getWealth() + "'" +
            ", favour='" + getFavour() + "'" +
            ", turn='" + getTurn() + "'" +
            "}";
    }
}
