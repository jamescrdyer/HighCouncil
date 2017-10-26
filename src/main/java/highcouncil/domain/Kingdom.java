package highcouncil.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Kingdom.
 */
@Entity
@Table(name = "kingdom")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Kingdom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "health", nullable = false)
    private Integer health;

    @OneToOne(mappedBy = "kingdom")
    @JsonIgnore
    private Game game;

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

    public Kingdom piety(Integer piety) {
        this.piety = piety;
        return this;
    }

    public void setPiety(Integer piety) {
        this.piety = piety;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Kingdom popularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMilitary() {
        return military;
    }

    public Kingdom military(Integer military) {
        this.military = military;
        return this;
    }

    public void setMilitary(Integer military) {
        this.military = military;
    }

    public Integer getWealth() {
        return wealth;
    }

    public Kingdom wealth(Integer wealth) {
        this.wealth = wealth;
        return this;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

    public Integer getHealth() {
        return health;
    }

    public Kingdom health(Integer health) {
        this.health = health;
        return this;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Game getGame() {
        return game;
    }

    public Kingdom game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
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
        Kingdom kingdom = (Kingdom) o;
        if (kingdom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), kingdom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Kingdom{" +
            "id=" + getId() +
            ", piety='" + getPiety() + "'" +
            ", popularity='" + getPopularity() + "'" +
            ", military='" + getMilitary() + "'" +
            ", wealth='" + getWealth() + "'" +
            ", health='" + getHealth() + "'" +
            "}";
    }
}
