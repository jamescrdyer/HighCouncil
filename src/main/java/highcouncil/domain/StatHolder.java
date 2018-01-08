package highcouncil.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class StatHolder {
    @NotNull
    @Column(name = "piety")
	protected Integer piety;

    @NotNull
    @Column(name = "popularity", nullable = false)
    protected Integer popularity;

    @NotNull
    @Column(name = "military", nullable = false)
	protected Integer military;

    @NotNull
    @Column(name = "wealth", nullable = false)
    protected Integer wealth;

    public Integer getPiety() {
        return piety;
    }

    public void setPiety(Integer piety) {
        this.piety = piety;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMilitary() {
        return military;
    }

    public void setMilitary(Integer military) {
        this.military = military;
    }

    public Integer getWealth() {
        return wealth;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

}
