package highcouncil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import highcouncil.domain.enumeration.Action;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "turn", nullable = false)
    private Integer turn;

    @NotNull
    @Column(name = "piety", nullable = false)
    private Integer piety;

    @NotNull
    @Column(name = "popularity", nullable = false)
    private Integer popularity;

    @NotNull
    @Column(name = "military", nullable = false)
    private Integer military;

    @NotNull
    @Column(name = "wealth", nullable = false)
    private Integer wealth;

    @NotNull
    @Column(name = "favour", nullable = false)
    private Integer favour;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurn() {
        return turn;
    }

    public Orders turn(Integer turn) {
        this.turn = turn;
        return this;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Integer getPiety() {
        return piety;
    }

    public Orders piety(Integer piety) {
        this.piety = piety;
        return this;
    }

    public void setPiety(Integer piety) {
        this.piety = piety;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Orders popularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMilitary() {
        return military;
    }

    public Orders military(Integer military) {
        this.military = military;
        return this;
    }

    public void setMilitary(Integer military) {
        this.military = military;
    }

    public Integer getWealth() {
        return wealth;
    }

    public Orders wealth(Integer wealth) {
        this.wealth = wealth;
        return this;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

    public Integer getFavour() {
        return favour;
    }

    public Orders favour(Integer favour) {
        this.favour = favour;
        return this;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public Action getAction() {
        return action;
    }

    public Orders action(Action action) {
        this.action = action;
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Game getGame() {
        return game;
    }

    public Orders game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public Orders player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
        Orders orders = (Orders) o;
        if (orders.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orders.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", turn='" + getTurn() + "'" +
            ", piety='" + getPiety() + "'" +
            ", popularity='" + getPopularity() + "'" +
            ", military='" + getMilitary() + "'" +
            ", wealth='" + getWealth() + "'" +
            ", favour='" + getFavour() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }
}
