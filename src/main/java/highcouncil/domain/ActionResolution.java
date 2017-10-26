package highcouncil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import highcouncil.domain.enumeration.Action;

/**
 * A ActionResolution.
 */
@Entity
@Table(name = "action_resolution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActionResolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    @Column(name = "code_normal")
    private String codeNormal;

    @Column(name = "code_chancellor")
    private String codeChancellor;

    @Column(name = "code_kingdom")
    private String codeKingdom;

    @ManyToOne
    private OrderResolution orderResolution;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public ActionResolution action(Action action) {
        this.action = action;
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getCodeNormal() {
        return codeNormal;
    }

    public ActionResolution codeNormal(String codeNormal) {
        this.codeNormal = codeNormal;
        return this;
    }

    public void setCodeNormal(String codeNormal) {
        this.codeNormal = codeNormal;
    }

    public String getCodeChancellor() {
        return codeChancellor;
    }

    public ActionResolution codeChancellor(String codeChancellor) {
        this.codeChancellor = codeChancellor;
        return this;
    }

    public void setCodeChancellor(String codeChancellor) {
        this.codeChancellor = codeChancellor;
    }

    public String getCodeKingdom() {
        return codeKingdom;
    }

    public ActionResolution codeKingdom(String codeKingdom) {
        this.codeKingdom = codeKingdom;
        return this;
    }

    public void setCodeKingdom(String codeKingdom) {
        this.codeKingdom = codeKingdom;
    }

    public OrderResolution getOrderResolution() {
        return orderResolution;
    }

    public ActionResolution orderResolution(OrderResolution orderResolution) {
        this.orderResolution = orderResolution;
        return this;
    }

    public void setOrderResolution(OrderResolution orderResolution) {
        this.orderResolution = orderResolution;
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
        ActionResolution actionResolution = (ActionResolution) o;
        if (actionResolution.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actionResolution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActionResolution{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", codeNormal='" + getCodeNormal() + "'" +
            ", codeChancellor='" + getCodeChancellor() + "'" +
            ", codeKingdom='" + getCodeKingdom() + "'" +
            "}";
    }
}
