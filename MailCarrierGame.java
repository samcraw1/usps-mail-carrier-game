import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

public class MailCarrierGame {
    public static void main(String[] args) {

        // Ask for username
        String username = JOptionPane.showInputDialog("Enter your USPS carrier name:");
        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }

        ApiClient apiClient = new ApiClient();
        User user = apiClient.getOrCreateUser(username);
        System.out.println("Playing as " + user.getUsername());

        // Default outfit - HomeScreen will let user change colors before Play
        Outfit outfit = new Outfit("#00427B", "#23408C");

        // CardLayout container holds both screens; shows one at a time
        CardLayout cards = new CardLayout();
        JPanel cardContainer = new JPanel(cards);
        cardContainer.setPreferredSize(new Dimension(768, 576));

        JFrame window = new JFrame("Mail Carrier Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // HomeScreen is created with a Play-callback. Array trick lets the lambda
        // reference homeRef[0] AFTER homeRef[0] gets assigned (effectively-final workaround).
        HomeScreen[] homeRef = new HomeScreen[1];
        homeRef[0] = new HomeScreen(outfit, () -> {
            // When PLAY clicked: create GamePanel with chosen level + outfit,
            // add it as a card, swap to it, start the game thread.
            GamePanel gamePanel = new GamePanel(
                user.getUsername(),
                apiClient,
                outfit,
                homeRef[0].getSelectedLevel()
            );
            cardContainer.add(gamePanel, "GAME");
            cards.show(cardContainer, "GAME");
            gamePanel.startGameThread();
            gamePanel.requestFocus();
        });

        cardContainer.add(homeRef[0], "HOME");

        window.add(cardContainer);
        window.pack();
        window.setVisible(true);
        window.setResizable(false);
    }
}
