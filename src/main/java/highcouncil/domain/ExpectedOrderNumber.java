package highcouncil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExpectedOrderNumbers.
 */
@Entity
@Table(name = "expected_order_number")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExpectedOrderNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 3)
    @Column(name = "number_of_players", nullable = false)
    private Integer numberOfPlayers;

    @NotNull
    @Column(name = "player_number", nullable = false)
    private Integer playerNumber;

    @NotNull
    @Column(name = "orders_expected", nullable = false)
    private Integer ordersExpected;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public ExpectedOrderNumber numberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        return this;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public ExpectedOrderNumber playerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
        return this;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Integer getOrdersExpected() {
        return ordersExpected;
    }

    public ExpectedOrderNumber ordersExpected(Integer ordersExpected) {
        this.ordersExpected = ordersExpected;
        return this;
    }

    public void setOrdersExpected(Integer ordersExpected) {
        this.ordersExpected = ordersExpected;
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
        ExpectedOrderNumber expectedOrderNumbers = (ExpectedOrderNumber) o;
        if (expectedOrderNumbers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), expectedOrderNumbers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExpectedOrderNumbers{" +
            "id=" + getId() +
            ", numberOfPlayers='" + getNumberOfPlayers() + "'" +
            ", playerNumber='" + getPlayerNumber() + "'" +
            ", ordersExpected='" + getOrdersExpected() + "'" +
            "}";
    }
}
