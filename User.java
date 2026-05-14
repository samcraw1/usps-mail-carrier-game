public class User {
    private String username;
    private long bestTimeMs;
    private String role;

    public User(String username, long bestTimeMs) {
        this.username = username;
        this.bestTimeMs = bestTimeMs;
    }
public String getUsername() {
        return username;
    }

    public long getBestTimeMs() {
        return bestTimeMs;
    }

    public void setUsername(String username) {
        this.username = username;
    }
public void setBestTimeMs(long bestTimeMs) {
        this.bestTimeMs = bestTimeMs;
    }   

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
