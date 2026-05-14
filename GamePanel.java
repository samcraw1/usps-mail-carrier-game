import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    // FIELDS
    Thread gameThread;
    Player carrier;
    KeyHandler keyH = new KeyHandler();
    TileMap tileMap;                                     // initialized inside loadLevel()
    NPC oldlady = new NPC("Mrs. Johnson", 200, 200, "Thank you for delivering on time!", "person");
    NPC dog = new NPC("Rover", 300, 300, "Woof! Thanks for the treats!", "dog");
    String currentDialogue = "";   // holds whatever NPC is talking right now
    ArrayList<House> houses = new ArrayList<>();

    // USER + SCORING fields
    String username;
    ApiClient apiClient;
    long gameStartTimeMs;
    long finalElapsedMs;
    boolean scoreSubmitted = false;
    List<User> leaderboard;
    String currentRole = "CARRIER";   

    // Level progression state
    int currentLevel = 0;
    String[] mapFiles = {
        "assets/map01.txt",
        "assets/map02.txt",
        "assets/map03.txt",
        "assets/map04.txt"   // driving level
    };

    Color[] houseColors = {
        new Color(200, 100, 50),
        new Color(150, 150, 50),
        new Color(100, 200, 50),
        new Color(50, 150, 200),
        new Color(200, 50, 150)
    };

    // CONSTRUCTOR - takes username + apiClient so we can identify the player and talk to the backend.
    public GamePanel(String username, ApiClient apiClient, Outfit outfit, int startLevel) {
        this.username = username;
        this.apiClient = apiClient;

        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.BLACK);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        carrier = new Player(username, outfit);

        // Record when the game starts (used to compute total time at the end).
        gameStartTimeMs = System.currentTimeMillis();

        // Load the first level (map01).
        loadLevel(startLevel);
        
    }

    // METHOD - load a map by index. Resets player + houses + tilemap.
    public void loadLevel(int levelIdx) {
        currentLevel = levelIdx;

        tileMap = new TileMap(mapFiles[levelIdx]);

        houses.clear();
        for (int row = 0; row < tileMap.rows; row++) {
            for (int col = 0; col < tileMap.cols; col++) {
                if (tileMap.mapData[row][col] == 3) {
                    Color randomColor = houseColors[(row * tileMap.cols + col) % houseColors.length];

                    int upDist = Integer.MAX_VALUE;
                    for (int r = row - 1; r >= 0; r--) {
                        int t = tileMap.mapData[r][col];
                        if (t == 1 || t == 5) { upDist = row - r; break; }
                    }
                    int downDist = Integer.MAX_VALUE;
                    for (int r = row + 1; r < tileMap.rows; r++) {
                        int t = tileMap.mapData[r][col];
                        if (t == 1 || t == 5) { downDist = r - row; break; }
                    }
                    int leftDist = Integer.MAX_VALUE;
                    for (int c = col - 1; c >= 0; c--) {
                        int t = tileMap.mapData[row][c];
                        if (t == 1 || t == 5) { leftDist = col - c; break; }
                    }
                    int rightDist = Integer.MAX_VALUE;
                    for (int c = col + 1; c < tileMap.cols; c++) {
                        int t = tileMap.mapData[row][c];
                        if (t == 1 || t == 5) { rightDist = c - col; break; }
                    }

                    String dir = "DOWN";
                    int min = downDist;
                    if (upDist < min)    { dir = "UP";    min = upDist; }
                    if (leftDist < min)  { dir = "LEFT";  min = leftDist; }
                    if (rightDist < min) { dir = "RIGHT"; min = rightDist; }

                    houses.add(new House(col * tileMap.tileSize, row * tileMap.tileSize, row * tileMap.cols + col, randomColor, dir, min));
                }
            }
        }

        carrier.packages = 5;

        // Driving level (index 3) - vehicle mode + faster + start ON the road.
        if (levelIdx == 3) {
            carrier.inVehicle = true;
            carrier.speed = 8;
            carrier.x = 48;
            carrier.y = 48;
        } else {
            carrier.inVehicle = false;
            carrier.speed = 4;
            carrier.x = 100;
            carrier.y = 100;
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        while (gameThread != null) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        int newX = carrier.x;
        int newY = carrier.y;

        if (keyH.upPressed)    newY -= carrier.speed;
        if (keyH.downPressed)  newY += carrier.speed;
        if (keyH.leftPressed)  newX -= carrier.speed;
        if (keyH.rightPressed) newX += carrier.speed;

        // Vehicle mode uses road-only collision; walking uses regular collision.
        boolean blocked = carrier.inVehicle
                ? tileMap.isSolidForVehicle(newX, newY, 48)
                : tileMap.isSolid(newX, newY, 48);
        if (!blocked) {
            carrier.x = newX;
            carrier.y = newY;
        }

        if (keyH.ePressed) {
            tryDeliver();
            keyH.ePressed = false;
        }

        if (keyH.tPressed) {
            tryTalk();
            keyH.tPressed = false;
        }

        // Route complete + SPACE pressed + more levels exist = advance
        if (carrier.packages == 0 && keyH.spacePressed && currentLevel + 1 < mapFiles.length) {
            loadLevel(currentLevel + 1);
            keyH.spacePressed = false;
        }

        // ALL levels complete + score not yet submitted = submit + fetch leaderboard
        if (carrier.packages == 0 && currentLevel + 1 == mapFiles.length && !scoreSubmitted) {
            finalElapsedMs = System.currentTimeMillis() - gameStartTimeMs;
            apiClient.submitScore(username, finalElapsedMs);
            leaderboard = apiClient.getLeaderboard();
            scoreSubmitted = true;

            // Find our own entry in the leaderboard and update the role badge
            // (backend may have just promoted us to SUPERVISOR)
            for (User entry : leaderboard) {
                if (entry.getUsername().equals(username)) {
                    currentRole = entry.getRole();
                    break;
                }
            }
        }
    }

    public void tryTalk() {
        if (isClose(oldlady.x, oldlady.y)) {
            currentDialogue = oldlady.name + ": " + oldlady.dialogue;
        } else if (isClose(dog.x, dog.y)) {
            currentDialogue = dog.name + ": " + dog.dialogue;
        } else {
            currentDialogue = "";
        }
    }

    public boolean isClose(int npcX, int npcY) {
        int dx = Math.abs(carrier.x - npcX);
        int dy = Math.abs(carrier.y - npcY);
        return dx < 60 && dy < 60;
    }

    public void tryDeliver() {
        int playerCol = (carrier.x + 24) / 48;
        int playerRow = (carrier.y + 24) / 48;

        int[][] neighbors = {
            {playerRow - 1, playerCol},
            {playerRow + 1, playerCol},
            {playerRow, playerCol - 1},
            {playerRow, playerCol + 1}
        };

        for (int[] spot : neighbors) {
            int row = spot[0];
            int col = spot[1];

            if (row >= 0 && row < tileMap.rows && col >= 0 && col < tileMap.cols) {
                if (tileMap.mapData[row][col] == 3 && carrier.packages > 0) {
                    tileMap.setTile(row, col, 4);
                    carrier.packages--;
                    return;
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        tileMap.draw(g);

        for (House h : houses) {
            h.draw(g);
        }

        oldlady.draw(g);
        dog.draw(g);
        carrier.draw(g);

        // HUD - level, packages, carrier name
        g.setColor(Color.WHITE);
        g.drawString("Level " + (currentLevel + 1) + " / " + mapFiles.length, 30, 30);
        g.drawString("Packages left: " + carrier.packages, 600, 30);
        g.drawString("Carrier: " + username, 30, 50);
        g.drawString("Rank: " + currentRole, 30, 70);

        if (!currentDialogue.isEmpty()) {
            g.setColor(Color.BLACK);
            g.fillRect(20, 500, 728, 50);
            g.setColor(Color.WHITE);
            g.drawString(currentDialogue, 40, 530);
        }

        if (carrier.packages == 0) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, 768, 576);
            g.setColor(Color.GREEN);
            g.drawString("ROUTE COMPLETE!", 340, 270);
            g.setColor(Color.WHITE);

            if (currentLevel + 1 < mapFiles.length) {
                g.drawString("All packages delivered. Press SPACE for the next route!", 240, 300);
            } else {
                g.setColor(Color.YELLOW);
                g.drawString("ALL ROUTES COMPLETE! You finished every neighborhood!", 230, 300);

                // Show total time
                long seconds = finalElapsedMs / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                g.setColor(Color.WHITE);
                g.drawString("Your total time: " + minutes + "m " + remainingSeconds + "s", 280, 330);

                // Show top 5 leaderboard
                if (leaderboard != null) {
                    g.setColor(Color.CYAN);
                    g.drawString("TOP 5 LEADERBOARD:", 290, 370);
                    g.setColor(Color.WHITE);
                    for (int i = 0; i < leaderboard.size() && i < 5; i++) {
                        User entry = leaderboard.get(i);
                        long s = entry.getBestTimeMs() / 1000;
                        long m = s / 60;
                        long rs = s % 60;
                        g.drawString((i + 1) + ". " + entry.getUsername() + " - " + m + "m " + rs + "s", 280, 395 + (i * 20));
                    }
                }
            }
        }
    }
}
