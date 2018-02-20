package highcouncil.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class StatHolder {
    @NotNull
    @Column(name = "piety")
	protected int piety = 0;

    @NotNull
    @Column(name = "popularity", nullable = false)
    protected int popularity = 0;

    @NotNull
    @Column(name = "military", nullable = false)
	protected int military = 0;

    @NotNull
    @Column(name = "wealth", nullable = false)
    protected int wealth = 0;

    public Integer getPiety() {
        return piety;
    }

    public void setPiety(Integer piety) {
        this.piety = piety;
    }

    public void modifyPiety(int pietyChange) {
        this.piety = piety + pietyChange;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public void modifyPopularity(int popularityChange) {
        this.popularity = popularity + popularityChange;
    }
    
    public Integer getMilitary() {
        return military;
    }

    public void setMilitary(Integer military) {
        this.military = military;
    }

    public void modifyMilitary(int militaryChange) {
        this.military = military + militaryChange;
    }
    
    public Integer getWealth() {
        return wealth;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

    public void modifyWealth(int wealthChange) {
        this.wealth = wealth + wealthChange;
    }
}
