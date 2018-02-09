package highcouncil.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Player entity.
 */
public class PlayerDTO implements Serializable {
	private static final long serialVersionUID = -3658394165251613137L;

	private Long id;

    private Integer piety;

    @NotNull
    private Integer popularity;

    @NotNull
    private Integer military;

    @NotNull
    private Integer wealth;

    @NotNull
    private Integer favour;

    private Integer ordersExpected;

    private Boolean chancellor;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private Boolean phaseLocked;

    @NotNull
    @Min(value = 0)
    private Integer penalty;

    private Integer score;

    private Long gameId;

    private Long userId;

    private String userLogin;

    private Set<CardDTO> hands = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getFavour() {
        return favour;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public Boolean isChancellor() {
        return chancellor;
    }

    public void setChancellor(Boolean chancellor) {
        this.chancellor = chancellor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isPhaseLocked() {
        return phaseLocked;
    }

    public void setPhaseLocked(Boolean phaseLocked) {
        this.phaseLocked = phaseLocked;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

	public Integer getOrdersExpected() {
		return ordersExpected;
	}

	public void setOrdersExpected(Integer ordersExpected) {
		this.ordersExpected = ordersExpected;
	}
	
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Set<CardDTO> getHands() {
        return hands;
    }

    public void setHands(Set<CardDTO> cards) {
        this.hands = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerDTO playerDTO = (PlayerDTO) o;
        if(playerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
            "id=" + getId() +
            ", piety='" + getPiety() + "'" +
            ", popularity='" + getPopularity() + "'" +
            ", military='" + getMilitary() + "'" +
            ", wealth='" + getWealth() + "'" +
            ", favour='" + getFavour() + "'" +
            ", chancellor='" + isChancellor() + "'" +
            ", name='" + getName() + "'" +
            ", phaseLocked='" + isPhaseLocked() + "'" +
            ", penalty='" + getPenalty() + "'" +
            ", ordersExpected='" + getOrdersExpected() + "'" +
            ", score='" + getScore() + "'" +
            "}";
    }
}
