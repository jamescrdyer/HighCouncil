package highcouncil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "piety")
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

    @Column(name = "chancellor")
    private Boolean chancellor;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    private Game game;

    @ManyToOne
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "player_hand",
               joinColumns = @JoinColumn(name="players_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="hands_id", referencedColumnName="id"))
    private Set<Card> hands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPiety() {
        return piety;
    }

    public Player piety(Integer piety) {
        this.piety = piety;
        return this;
    }

    public void setPiety(Integer piety) {
        this.piety = piety;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Player popularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMilitary() {
        return military;
    }

    public Player military(Integer military) {
        this.military = military;
        return this;
    }

    public void setMilitary(Integer military) {
        this.military = military;
    }

    public Integer getWealth() {
        return wealth;
    }

    public Player wealth(Integer wealth) {
        this.wealth = wealth;
        return this;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

    public Integer getFavour() {
        return favour;
    }

    public Player favour(Integer favour) {
        this.favour = favour;
        return this;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public Boolean isChancellor() {
        return chancellor;
    }

    public Player chancellor(Boolean chancellor) {
        this.chancellor = chancellor;
        return this;
    }

    public void setChancellor(Boolean chancellor) {
        this.chancellor = chancellor;
    }

    public String getName() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public Player game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public Player user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Card> getHands() {
        return hands;
    }

    public Player hands(Set<Card> cards) {
        this.hands = cards;
        return this;
    }

    public Player addHand(Card card) {
        this.hands.add(card);
        return this;
    }

    public Player removeHand(Card card) {
        this.hands.remove(card);
        return this;
    }

    public void setHands(Set<Card> cards) {
        this.hands = cards;
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
        Player player = (Player) o;
        if (player.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", piety='" + getPiety() + "'" +
            ", popularity='" + getPopularity() + "'" +
            ", military='" + getMilitary() + "'" +
            ", wealth='" + getWealth() + "'" +
            ", favour='" + getFavour() + "'" +
            ", chancellor='" + isChancellor() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
