// Quick test to verify ApiClient stub methods work.
// Run with: java ApiClientTest
public class ApiClientTest {
    public static void main(String[] args) {
        ApiClient api = new ApiClient();

        System.out.println("=== getOrCreateUser ===");
        User sam = api.getOrCreateUser("Sam");
        System.out.println("Got user: " + sam.getUsername() + ", best time: " + sam.getBestTimeMs());

        System.out.println("\n=== submitScore ===");
        api.submitScore("Sam", 45230);

        System.out.println("\n=== getLeaderboard ===");
        for (User u : api.getLeaderboard()) {
            System.out.println(u.getUsername() + " - " + u.getBestTimeMs() + " ms");
        }
    }
}
