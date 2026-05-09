import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    // FIELDS
    Thread gameThread;
    Player carrier;
    KeyHandler keyH = new KeyHandler();
    TileMap tileMap = new TileMap("assets/map01.txt");
    NPC oldlady = new NPC("Mrs. Johnson", 200, 200, "Thank you for delivering on time!");
    NPC dog = new NPC("Rover", 300, 300, "Woof! Thanks for the treats!");
    String currentDialogue = "";   // holds whatever NPC is talking right now
    ArrayList<House> houses = new ArrayList<>();
    
    // CONSTRUCTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.BLACK);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        carrier = new Player("Sam");

         Color[] houseColors = {
            new Color(200, 100, 50),
            new Color(150, 150, 50),
            new Color(100, 200, 50),
            new Color(50, 150, 200),
            new Color(200, 50, 150)
        };


        // create House objects for each house tile on the map
        for (int row = 0; row < tileMap.rows; row++) {
            for (int col = 0; col < tileMap.cols; col++) {
                if (tileMap.mapData[row][col] == 3) {
                    Color randomColor = houseColors[(row * tileMap.cols + col) % houseColors.length];

                    // find nearest road in each direction (looking up to the edge of the map)
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

                    // pick the direction with the shortest distance
                    String dir = "DOWN";
                    int min = downDist;
                    if (upDist < min)    { dir = "UP";    min = upDist; }
                    if (leftDist < min)  { dir = "LEFT";  min = leftDist; }
                    if (rightDist < min) { dir = "RIGHT"; min = rightDist; }

                    houses.add(new House(col * tileMap.tileSize, row * tileMap.tileSize, row * tileMap.cols + col, randomColor, dir, min));
                }
            }
        }
    }

    // METHOD - starts the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // METHOD - the game loop (runs ~60 fps)
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

    // METHOD - update game state — moves player IF the new position isn't a solid tile
    public void update() {
        // figure out where the player wants to move
        int newX = carrier.x;
        int newY = carrier.y;

        if (keyH.upPressed)    newY -= carrier.speed;
        if (keyH.downPressed)  newY += carrier.speed;
        if (keyH.leftPressed)  newX -= carrier.speed;
        if (keyH.rightPressed) newX += carrier.speed;

        // only actually move if the new spot isn't a wall
        if (!tileMap.isSolid(newX, newY, 48)) {
            carrier.x = newX;
            carrier.y = newY;
        }

        // try to deliver if E was pressed
        if (keyH.ePressed) {
            tryDeliver();
            keyH.ePressed = false;   // reset so it only fires once per press
        }

        // try to talk if T was pressed
        if (keyH.tPressed) {
            tryTalk();
            keyH.tPressed = false;
        }
    }

    // METHOD - check if any NPC is close, show their dialogue
    public void tryTalk() {
        // distance from player to each NPC
        if (isClose(oldlady.x, oldlady.y)) {
            currentDialogue = oldlady.name + ": " + oldlady.dialogue;
        } else if (isClose(dog.x, dog.y)) {
            currentDialogue = dog.name + ": " + dog.dialogue;
        } else {
            currentDialogue = "";   // no one nearby
        }
    }

    // helper - is the player close to this spot? (within 60 pixels)
    public boolean isClose(int npcX, int npcY) {
        int dx = Math.abs(carrier.x - npcX);
        int dy = Math.abs(carrier.y - npcY);
        return dx < 60 && dy < 60;
    }

    // METHOD - try to deliver to a nearby house
    public void tryDeliver() {
        // figure out which tile the player is on (use center of player)
        int playerCol = (carrier.x + 24) / 48;
        int playerRow = (carrier.y + 24) / 48;

        // check the 4 tiles next to the player (up, down, left, right)
        int[][] neighbors = {
            {playerRow - 1, playerCol},  // up
            {playerRow + 1, playerCol},  // down
            {playerRow, playerCol - 1},  // left
            {playerRow, playerCol + 1}   // right
        };

        for (int[] spot : neighbors) {
            int row = spot[0];
            int col = spot[1];

            // make sure this tile is actually on the map
            if (row >= 0 && row < 10 && col >= 0 && col < 10) {

                // is this neighbor an undelivered house, and do we have packages?
                if (tileMap.mapData[row][col] == 3 && carrier.packages > 0) {
                    tileMap.setTile(row, col, 4);   // mark it delivered
                    carrier.packages--;              // one less package
                    return;                          // stop after one delivery
                }
            }
        }
    }

    // METHOD - draw to the screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        tileMap.draw(g);

        // draw all the houses on top of the tiles
        for (House h : houses) {
            h.draw(g);
        }

        oldlady.draw(g);
        dog.draw(g);
        carrier.draw(g);
        

        // show package count in top corner
        g.setColor(Color.WHITE);
        g.drawString("Packages left: " + carrier.packages, 600, 30);

        // show NPC dialogue at the bottom if there is one
        if (!currentDialogue.isEmpty()) {
            g.setColor(Color.BLACK);
            g.fillRect(20, 500, 728, 50);
            g.setColor(Color.WHITE);
            g.drawString(currentDialogue, 40, 530);
        }

        // win screen if all packages delivered
        if (carrier.packages == 0) {
            g.setColor(new Color(0, 0, 0, 200));   // semi-transparent black overlay
            g.fillRect(0, 0, 768, 576);
            g.setColor(Color.GREEN);
            g.drawString("ROUTE COMPLETE!", 340, 280);
            g.setColor(Color.WHITE);
            g.drawString("All packages delivered. Great job!", 290, 310);
        }
    }
}
