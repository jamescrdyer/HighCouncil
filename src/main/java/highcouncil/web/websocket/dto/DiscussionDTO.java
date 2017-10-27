package highcouncil.web.websocket.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for storing a user's activity.
 */
public class DiscussionDTO {

    private List<String> toLogins = new ArrayList<String>();

    private String fromLogin;

    private long gameId;

    private String message;

    private Instant time;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getFromLogin() {
        return fromLogin;
    }

    public void setFromLogin(String fromLogin) {
        this.fromLogin = fromLogin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getToLogins() {
        return toLogins;
    }

    public void setToLogins(List<String> toLogins) {
        this.toLogins = toLogins;
    }
    
    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DiscussionDTO{" +
            "gameId=" + gameId + 
            ", fromLogin='" + fromLogin + '\'' +
            ", toLogins.size=" + toLogins.size() + 
            ", message='" + message + '\'' + 
            ", time='" + time + '\'' +
            '}';
    }
}
