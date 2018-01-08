package highcouncil.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects;

import highcouncil.domain.enumeration.Phase;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase")
    private Phase phase;

    @NotNull
    @Min(value = 1)
    @Column(name = "turn", nullable = false)
    private Integer turn;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Player> players = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Kingdom kingdom;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Deck deck;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public Game timeLimitSeconds(Integer timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
        return this;
    }

    public void setTimeLimitSeconds(Integer timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public Phase getPhase() {
        return phase;
    }

    public Game phase(Phase phase) {
        this.phase = phase;
        return this;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Integer getTurn() {
        return turn;
    }

    public Game turn(Integer turn) {
        this.turn = turn;
        return this;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    @JsonIgnore
    public List<Player> getPlayersList() {
        return players.stream().sorted((p1,p2) -> p1.getId().compareTo(p2.getId())).collect(Collectors.toList());
    }

    public Game players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Game addPlayer(Player player) {
        this.players.add(player);
        player.setGame(this);
        return this;
    }

    public Game removePlayer(Player player) {
        this.players.remove(player);
        player.setGame(null);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Game kingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
        return this;
    }

    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    public Deck getDeck() {
        return deck;
    }

    public Game deck(Deck deck) {
        this.deck = deck;
        return this;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
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
        Game game = (Game) o;
        if (game.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", timeLimitSeconds='" + getTimeLimitSeconds() + "'" +
            ", phase='" + getPhase() + "'" +
            ", turn='" + getTurn() + "'" +
            "}";
    }
}
