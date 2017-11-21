package highcouncil.web.websocket.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for storing a user's activity.
 */
public class DiscussionDTO {

    private List<String> toUsers = new ArrayList<String>();

    private String fromUser;

    private long gameId;

    private String message;

    private Instant time;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
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
            ", fromUser='" + fromUser + '\'' +
            ", toLogins.size=" + toUsers.size() + 
            ", message='" + message + '\'' + 
            ", time='" + time + '\'' +
            '}';
    }
}
