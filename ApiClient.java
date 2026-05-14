import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Real HTTP client that talks to the Spring Boot leaderboard API.
// Override the base URL with `-DapiUrl=http://localhost:8080` for local testing.
public class ApiClient {

    private static final String DEFAULT_BASE_URL =
        "http://vehicle-scanner-env.eba-mpgirmpk.us-east-1.elasticbeanstalk.com";

    private final String baseUrl;
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public ApiClient() {
        this.baseUrl = System.getProperty("apiUrl", DEFAULT_BASE_URL);
        System.out.println("ApiClient using: " + baseUrl);
    }

    // POST /api/users  body: {"username":"Sam"}  -> returns User with bestTimeMs
    public User getOrCreateUser(String username) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/users"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"username\":\"" + escape(username) + "\"}"))
                    .build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            return parseUser(res.body());
        } catch (Exception e) {
            System.err.println("getOrCreateUser failed: " + e.getMessage());
            return new User(username, 0);   // fallback so game still runs offline
        }
    }

    // POST /api/scores  body: {"username":"Sam","totalTimeMs":45000}
    public void submitScore(String username, long timeMs) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/scores"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"username\":\"" + escape(username) + "\",\"totalTimeMs\":" + timeMs + "}"))
                    .build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("submitScore status: " + res.statusCode());
        } catch (Exception e) {
            System.err.println("submitScore failed: " + e.getMessage());
        }
    }

    // GET /api/leaderboard -> returns top 10 Users sorted by bestTimeMs
    public List<User> getLeaderboard() {
        List<User> list = new ArrayList<>();
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/leaderboard"))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            // Parse each {"username":"...","bestTimeMs":NUMBER} object out of the array.
            Pattern p = Pattern.compile(
                    "\\{\\s*\"username\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"bestTimeMs\"\\s*:\\s*(\\d+)\\s*\\}");
            Matcher m = p.matcher(res.body());
            while (m.find()) {
                User u = new User(m.group(1), Long.parseLong(m.group(2)));
                u.setRole(extractRole(m.group()));
                list.add(u);
            }
        } catch (Exception e) {
            System.err.println("getLeaderboard failed: " + e.getMessage());
        }
        return list;
    }

    // Minimal JSON-safe escaping for usernames (handles quotes and backslashes).
    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Parse {"username":"Sam","bestTimeMs":0,...} into a User
    private User parseUser(String json) {
        Matcher m = Pattern.compile(
                "\"username\"\\s*:\\s*\"([^\"]+)\".*?\"bestTimeMs\"\\s*:\\s*(\\d+)",
                Pattern.DOTALL).matcher(json);
        if (m.find()) {
            User u = new User(m.group(1), Long.parseLong(m.group(2)));
            u.setRole(extractRole(json));
            return u;
        }
        return new User("Unknown", 0);
    }

    // Pull "CARRIER" or "SUPERVISOR" out of a JSON blob. Defaults to "CARRIER" if missing.
    private String extractRole(String json) {
        Matcher m = Pattern.compile("\"role\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
        return m.find() ? m.group(1) : "CARRIER";
    }
}
