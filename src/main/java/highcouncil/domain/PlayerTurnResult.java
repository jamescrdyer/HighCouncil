package highcouncil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import highcouncil.domain.enumeration.Action;

/**
 * A PlayerTurnResult.
 */
@Entity
@Table(name = "player_turn_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlayerTurnResult extends StatHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "favour")
    private int favour = 0;

    @Column(name = "penalty")
    private int penalty = 0;

    @Column(name = "turn")
    private int turn = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Player player;

    @ManyToOne
    private TurnResult turnResult;

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

    public PlayerTurnResult favour(int favour) {
        this.favour = favour;
        return this;
    }

    public void setFavour(int favour) {
        this.favour = favour;
    }

    public void modifyFavour(int favourChange) {
        this.favour += favourChange;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public PlayerTurnResult penalty(int penalty) {
        this.penalty = penalty;
        return this;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public Integer getTurn() {
        return turn;
    }

    public PlayerTurnResult turn(Integer turn) {
        this.turn = turn;
        return this;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Action getAction() {
        return action;
    }

    public PlayerTurnResult action(Action action) {
        this.action = action;
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Game getGame() {
        return game;
    }

    public PlayerTurnResult game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerTurnResult player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TurnResult getTurnResult() {
        return turnResult;
    }

    public PlayerTurnResult turnResult(TurnResult turnResult) {
        this.turnResult = turnResult;
        return this;
    }

    public void setTurnResult(TurnResult turnResult) {
        this.turnResult = turnResult;
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
        PlayerTurnResult playerTurnResult = (PlayerTurnResult) o;
        if (playerTurnResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerTurnResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlayerTurnResult{" +
            "id=" + getId() +
            ", piety='" + getPiety() + "'" +
            ", popularity='" + getPopularity() + "'" +
            ", military='" + getMilitary() + "'" +
            ", wealth='" + getWealth() + "'" +
            ", favour='" + getFavour() + "'" +
            ", penalty='" + getPenalty() + "'" +
            ", turn='" + getTurn() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }
}
