import javax.swing.JFrame;

public class MailCarrierGame {
    public static void main(String[] args) {
        
        JFrame window = new JFrame("Mail Carrier Game");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setTitle("Mail Carrier Game");
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setVisible(true);
        window.setResizable(false);

        gamePanel.startGameThread();
        gamePanel.requestFocus();
    }

}
    