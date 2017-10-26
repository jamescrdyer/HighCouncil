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
 * A Deck.
 */
@Entity
@Table(name = "deck")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Deck implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "deck")
    @JsonIgnore
    private Game game;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "deck_card",
               joinColumns = @JoinColumn(name="decks_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="cards_id", referencedColumnName="id"))
    private Set<Card> cards = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "deck_discard",
               joinColumns = @JoinColumn(name="decks_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="discards_id", referencedColumnName="id"))
    private Set<Card> discards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public Deck game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public Deck cards(Set<Card> cards) {
        this.cards = cards;
        return this;
    }

    public Deck addCard(Card card) {
        this.cards.add(card);
        return this;
    }

    public Deck removeCard(Card card) {
        this.cards.remove(card);
        return this;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<Card> getDiscards() {
        return discards;
    }

    public Deck discards(Set<Card> cards) {
        this.discards = cards;
        return this;
    }

    public Deck addDiscard(Card card) {
        this.discards.add(card);
        return this;
    }

    public Deck removeDiscard(Card card) {
        this.discards.remove(card);
        return this;
    }

    public void setDiscards(Set<Card> cards) {
        this.discards = cards;
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
        Deck deck = (Deck) o;
        if (deck.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deck.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Deck{" +
            "id=" + getId() +
            "}";
    }
}
