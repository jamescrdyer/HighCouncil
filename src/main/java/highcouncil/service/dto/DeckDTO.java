package highcouncil.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Deck entity.
 */
public class DeckDTO implements Serializable {

    private Long id;

    private Set<CardDTO> cards = new HashSet<>();

    private Set<CardDTO> discards = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }

    public Set<CardDTO> getDiscards() {
        return discards;
    }

    public void setDiscards(Set<CardDTO> cards) {
        this.discards = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeckDTO deckDTO = (DeckDTO) o;
        if(deckDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deckDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeckDTO{" +
            "id=" + getId() +
            "}";
    }
}
