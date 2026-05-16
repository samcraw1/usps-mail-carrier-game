import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    private int selectedLevel = 0;

    public HomeScreen(Outfit outfit, Runnable onPlayClicked) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(20, 30, 50));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // ---- TITLE ----
        JLabel title = new JLabel("USPS Mail Carrier");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(30));

        // ---- LEVEL SELECT ----
        JLabel levelLabel = new JLabel("Pick a level");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(levelLabel);
        add(Box.createVerticalStrut(8));

        JPanel levelRow = new JPanel();
        levelRow.setBackground(new Color(20, 30, 50));
        for (int i = 0; i < 4; i++) {
            final int levelIdx = i;
            JButton btn = new JButton("Level " + (i + 1));
            btn.addActionListener(e -> selectedLevel = levelIdx);
            levelRow.add(btn);
        }
        add(levelRow);
        add(Box.createVerticalStrut(30));

        // ---- HAT COLOR ----
        JLabel hatLabel = new JLabel("Hat color");
        hatLabel.setForeground(Color.WHITE);
        hatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(hatLabel);
        add(Box.createVerticalStrut(8));

        String[] hatColors = { "#00427B", "#1A1A1A", "#1A5D1A", "#80162B", "#C8A268" };
        JPanel hatRow = new JPanel();
        hatRow.setBackground(new Color(20, 30, 50));
        for (String hex : hatColors) {
            final String chosenHex = hex;
            JButton swatch = new JButton();
            swatch.setBackground(Color.decode(hex));
            swatch.setPreferredSize(new Dimension(40, 40));
            swatch.setOpaque(true);
            swatch.setBorderPainted(false);
            swatch.addActionListener(e -> outfit.setHatColor(chosenHex));
            hatRow.add(swatch);
        }
        add(hatRow);
        add(Box.createVerticalStrut(20));

        // ---- SHIRT COLOR ----
        JLabel shirtLabel = new JLabel("Shirt color");
        shirtLabel.setForeground(Color.WHITE);
        shirtLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(shirtLabel);
        add(Box.createVerticalStrut(8));

        String[] shirtColors = { "#23408C", "#0B1E4F", "#4A5568", "#1F4FC7", "#2D2D2D" };
        JPanel shirtRow = new JPanel();
        shirtRow.setBackground(new Color(20, 30, 50));
        for (String hex : shirtColors) {
            final String chosenHex = hex;
            JButton swatch = new JButton();
            swatch.setBackground(Color.decode(hex));
            swatch.setPreferredSize(new Dimension(40, 40));
            swatch.setOpaque(true);
            swatch.setBorderPainted(false);
            swatch.addActionListener(e -> outfit.setShirtColor(chosenHex));
            shirtRow.add(swatch);
        }
        add(shirtRow);
        add(Box.createVerticalStrut(40));


        JLabel postmasterLabel = new JLabel("Postmaster Mode");
        postmasterLabel.setForeground(Color.WHITE);
        postmasterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(postmasterLabel);
        add(Box.createVerticalStrut(8));


        JPanel postmasterRow = new JPanel();
        postmasterRow.setBackground(new Color(20, 30, 50));

        JButton fwBtn = new JButton("Fort Worth");
        fwBtn.addActionListener(e -> FortWorthDashboard.main(new String[0]));
        postmasterRow.add(fwBtn);

        JButton lrBtn = new JButton("Little Rock");
        lrBtn.addActionListener(e -> LittleRockDashboard.main(new String[0]));
        postmasterRow.add(lrBtn);

        JButton atlBtn = new JButton("Atlanta");
        atlBtn.addActionListener(e -> AtlantaDashboard.main(new String[0]));
        postmasterRow.add(atlBtn);

        add(postmasterRow);
        add(Box.createVerticalStrut(30));

        // ---- PLAY BUTTON ----
        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setPreferredSize(new Dimension(200, 50));
        playButton.setMaximumSize(new Dimension(200, 50));
        playButton.addActionListener(e -> onPlayClicked.run());
        add(playButton);
    }

    // Getter so MailCarrierGame can read which level the user picked.
    public int getSelectedLevel() {
        return selectedLevel;
    }
}
